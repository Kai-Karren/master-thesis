package gateway

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strings"

	"github.com/Kai-Karren/chatwoot-golang-client/chatwootclient"
	"github.com/Kai-Karren/cui-gateway/rasa"
	"github.com/Kai-Karren/cui-gateway/utils"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

type Gateway struct {
	ChatwootClient              chatwootclient.ChatwootClient
	RasaURL                     string
	RasaAPIKey                  string
	ErrorMessage                string
	InboxID                     int
	InteractionCompletedMessage string
	EnableChatwootIntegration   bool
}

type GatewayRequest struct {
	ConversationID         string `json:"conversation_id"`
	ChatwootConversationID int    `json:"chatwoot_conversation_id"`
	Text                   string `json:"text"`
}

type GatewayResponse struct {
	ConversationID         string    `json:"conversation_id"`
	Messages               []Message `json:"messages"`
	DialogueEngine         string    `json:"dialogue_engine"`
	ChatwootConversationID int       `json:"chatwoot_conversation_id"`
}

type Message struct {
	ID     string                 `json:"id"`
	Type   string                 `json:"type"`
	Text   string                 `json:"text"`
	Custom map[string]interface{} `json:"custom"`
}

func (gateway *Gateway) HandleMessages(c *gin.Context) {

	var request GatewayRequest

	if err := c.BindJSON(&request); err != nil {
		utils.Logger.Error(err.Error())
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": "500", "message": err.Error()})
		return
	}

	utils.Logger.Info("Received the request: ", zap.Any("request", request))

	// Because Rasa API returns an error if the user input contains quotes, quotes are removed from the user input
	request.Text = gateway.removeQuotesInUserInput(request.Text)

	botResponse, err := gateway.GetResponseFromBot(request.ConversationID, request.Text)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while getting the response from the bot. %s", err)
		if request.ChatwootConversationID != 0 {
			gateway.SetErrorLabel(request.ChatwootConversationID)
		}
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": "500", "message": err.Error()})
		return
	}

	gatewayResponse := gateway.ConvertRasaResponse(botResponse)

	gatewayResponse.ConversationID = request.ConversationID
	gatewayResponse.DialogueEngine = "rasa"

	// Add message to chatwoot

	if gateway.EnableChatwootIntegration {

		id, err := gateway.SendToChatwoot(request.ConversationID, request, botResponse)

		utils.Logger.Info("Chatwoot conversation id", zap.Any("id", id))

		if err != nil {
			utils.LoggerSugar.Errorf("An error occurred while sending the message to chatwoot. %s", err)
			return
		}

		gatewayResponse.ChatwootConversationID = id

	}

	c.IndentedJSON(http.StatusOK, gatewayResponse)

}

func (gateway *Gateway) removeQuotesInUserInput(userInput string) string {

	return strings.ReplaceAll(userInput, "\"", "")

}

func (gateway *Gateway) ConvertRasaResponse(rasaResponse rasa.RasaResponse) GatewayResponse {

	var messages []Message

	for _, message := range rasaResponse.Messages {

		if message.Custom != nil {
			messages = append(messages, Message{
				ID:     uuid.New().String(),
				Type:   "custom",
				Custom: message.Custom,
			})
		} else {

			messages = append(messages, Message{
				ID:   uuid.New().String(),
				Type: "text",
				Text: message.Text,
			})
		}

	}

	return GatewayResponse{
		Messages: messages,
	}

}

func (gateway *Gateway) SendToChatwoot(conversationId string, request GatewayRequest, rasaResponse rasa.RasaResponse) (int, error) {

	id := request.ChatwootConversationID

	if id == 0 {

		contactResponse, err := gateway.ChatwootClient.CreateContact(chatwootclient.CreateContactRequest{
			Name:    uuid.NewString(),
			InboxID: gateway.InboxID,
		})

		if err != nil {
			utils.LoggerSugar.Errorf("An error occurred while creating a new contact. %s", err)
		}

		utils.Logger.Info("Created a new contact", zap.Any("response", contactResponse))

		sourceId := contactResponse.Payload.Contact.ContactInboxes[len(contactResponse.Payload.Contact.ContactInboxes)-1].SourceID

		conversationCreationResponse, err := gateway.ChatwootClient.CreateNewConversation(chatwootclient.CreateNewConversationRequest{
			SourceID: sourceId,
			InboxID:  gateway.InboxID,
		})

		if err != nil {
			utils.LoggerSugar.Errorf("An error occurred while creating a new conversation. %s", err)
		}

		utils.LoggerSugar.Info("Created a new conversation", zap.Any("response", conversationCreationResponse))

		id = conversationCreationResponse.ID

	}

	_, err := gateway.ChatwootClient.CreateIncomingMessage(id, request.Text)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while creating a new message. %s", err)
	}

	for _, message := range rasaResponse.Messages {

		if message.Text != "" {
			gateway.ChatwootClient.CreateOutgoingMessage(id, message.Text)
		}

	}

	return id, nil

}

func (gateway *Gateway) SetErrorLabel(chatwoot_conversation_id int) {

	gateway.ChatwootClient.AddLabel(chatwoot_conversation_id, "agent_bot_error")

}

func (gateway *Gateway) GetResponseFromBot(conversation_id string, message string) (rasa.RasaResponse, error) {

	/*
		{
		  	"sender": "test_user",
		  	"message": "Hey"
		}
	*/

	content := fmt.Sprintf(`{"sender": "%s", "message": "%s"}`, conversation_id, message)

	utils.Logger.Info(content)

	jsonBody := []byte(content)

	bodyReader := bytes.NewReader(jsonBody)

	url := fmt.Sprintf(gateway.RasaURL + "/webhooks/rest/webhook")

	request, err := http.NewRequest(http.MethodPost, url, bodyReader)

	request.Header.Set("Content-Type", "application/json")
	request.Header.Set("Accept", "application/json")
	request.Header.Set("apiKey", gateway.RasaAPIKey)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while creating the http request. %s", err)
		return rasa.NewRasaResponseFromRaw(nil), err
	}

	result, err := http.DefaultClient.Do(request)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while making the http request. %s", err)
		return rasa.NewRasaResponseFromRaw(nil), err
	}

	body, err := io.ReadAll(result.Body)

	if err != nil {
		utils.LoggerSugar.Errorf("client: could not read response body: %s", err)
		return rasa.NewRasaResponseFromRaw(nil), err
	}

	var rawRasaResponse rasa.RawRasaResponse

	if err := json.Unmarshal(body, &rawRasaResponse); err != nil {
		utils.Logger.Error("Can not unmarshal JSON")
	}

	utils.Logger.Info("Received the response from Rasa", zap.Any("response", rawRasaResponse))

	rasaResponse := rasa.NewRasaResponseFromRaw(rawRasaResponse)

	return rasaResponse, nil

}

func (gateway *Gateway) CreateConversation(sourceId string) (int, error) {

	response, err := gateway.ChatwootClient.CreateNewConversation(chatwootclient.CreateNewConversationRequest{
		SourceID: sourceId,
		InboxID:  gateway.InboxID,
	})

	if err != nil {
		return -1, err
	}

	return response.ID, nil

}
