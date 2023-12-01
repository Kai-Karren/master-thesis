package chatwoot

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
	"go.uber.org/zap"
)

type ChatwootRasaAgentBot struct {
	ChatwootClient              chatwootclient.ChatwootClient
	RasaURL                     string
	RasaAPIKey                  string
	ErrorMessage                string
	InboxID                     int
	InteractionCompletedMessage string
}

func NewChatwootRasaAgentBot(chatwootClient chatwootclient.ChatwootClient, inboxID int, rasaUrl string, rasaAPIKey string, errorMessage string) ChatwootRasaAgentBot {

	return ChatwootRasaAgentBot{
		ChatwootClient: chatwootClient,
		InboxID:        inboxID,
		RasaURL:        rasaUrl,
		RasaAPIKey:     rasaAPIKey,
		ErrorMessage:   errorMessage,
	}

}

func NewChatwootRasaAgentBotPreset(chatwootClient chatwootclient.ChatwootClient, inboxID int, rasaUrl string, rasaAPIKey string) ChatwootRasaAgentBot {

	return ChatwootRasaAgentBot{
		ChatwootClient:              chatwootClient,
		InboxID:                     inboxID,
		RasaURL:                     rasaUrl,
		RasaAPIKey:                  rasaAPIKey,
		ErrorMessage:                "Es tut mir leid. Es ist ein kritischer Fehler aufgetreten. Ich habe meine menschlichen Kollegen benachrichtigt.",
		InteractionCompletedMessage: "Sie haben das Ende Ihrer Interaktion mit dem System im Rahmen dieser Studie erreicht. Sie können den Chat nun schließen und auf den Link zum Fragebogen klicken. Der Fragebogen wird sich in einem eigenen Tab öffnen.",
	}

}

func (agentBot *ChatwootRasaAgentBot) HandleMessages(c *gin.Context) {

	var request Event

	if err := c.BindJSON(&request); err != nil {
		utils.Logger.Error(err.Error())
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": "500", "message": err.Error()})
		return
	}

	utils.Logger.Info("Received the request: ", zap.Any("request", request))

	// The Bot sends the first message
	if request.Event == "webwidget_triggered" {

		utils.LoggerSugar.Infof("Source Id %v", request.SourceID)

		var conversation_id int

		if request.CurrentConversation.ID != 0 {
			utils.LoggerSugar.Infof("A conversation already exists with id %d. No new conversation is created.", request.CurrentConversation.ID)
			return
		} else {
			con_id, err := agentBot.CreateConversation(request.SourceID)

			if err == nil {
				conversation_id = con_id
			} else {
				utils.Logger.Error(err.Error())
			}
		}

		responseFromBot, err := agentBot.GetResponseFromBot(conversation_id, "start")

		if err != nil {
			utils.LoggerSugar.Errorf("An error occurred while trying to get the response from the bot. %s", err.Error())
			agentBot.SetErrorLabelAndSendErrorMessageBack(conversation_id)
		} else {
			agentBot.SendResponseBack(conversation_id, responseFromBot)
		}

		return

	}

	if request.Event == "message_created" && request.MessageType == "incoming" {

		utils.Logger.Info("Received " + request.Content)

		rasaResponse, err := agentBot.GetResponseFromBot(request.Conversation.ID, request.Content)

		if err != nil {

			agentBot.SetErrorLabelAndSendErrorMessageBack(request.Conversation.ID)

		} else {

			err = agentBot.SendResponseBack(request.Conversation.ID, rasaResponse)

			if err != nil {
				utils.LoggerSugar.Errorf("The message could not be send back to chatwoot. %s", err.Error())
				agentBot.SetErrorLabelAndSendErrorMessageBack(request.Conversation.ID)
			}

			err = agentBot.ApplyCustomAttributes(request.Conversation.ID, rasaResponse)

			if err != nil {
				utils.LoggerSugar.Errorf("An error occurred while trying to apply the custom attributes. %s", err.Error())
			}

		}

	}

}

func (agentBot *ChatwootRasaAgentBot) SetErrorLabelAndSendErrorMessageBack(conversation_id int) {

	agentBot.ChatwootClient.AddLabel(conversation_id, "agent_bot_error")

	agentBot.SendResponseBack(conversation_id, rasa.NewRasaResponseSingleMessage(
		agentBot.ErrorMessage,
	))
}

func (agentBot *ChatwootRasaAgentBot) GetResponseFromBot(conversation_id int, message string) (rasa.RasaResponse, error) {

	/*
		{
		  	"sender": "test_user",
		  	"message": "Hey"
		}
	*/

	content := fmt.Sprintf(`{"sender": "%d", "message": "%s"}`, conversation_id, message)

	utils.Logger.Info(content)

	jsonBody := []byte(content)

	bodyReader := bytes.NewReader(jsonBody)

	url := fmt.Sprintf(agentBot.RasaURL + "/webhooks/rest/webhook")

	request, err := http.NewRequest(http.MethodPost, url, bodyReader)

	request.Header.Set("Content-Type", "application/json")
	request.Header.Set("Accept", "application/json")
	request.Header.Set("apiKey", agentBot.RasaAPIKey)

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

func (agentBot *ChatwootRasaAgentBot) CreateConversation(sourceId string) (int, error) {

	response, err := agentBot.ChatwootClient.CreateNewConversation(chatwootclient.CreateNewConversationRequest{
		SourceID: sourceId,
		InboxID:  agentBot.InboxID,
	})

	if err != nil {
		return -1, err
	}

	return response.ID, nil

}

func (agentBot *ChatwootRasaAgentBot) ApplyCustomAttributes(conversationId int, rasaResponse rasa.RasaResponse) error {

	if rasaResponse.HasCustom() {

		customAttributes := rasaResponse.GetCustom()

		value, ok := customAttributes["conversation_completed"]

		if ok {

			if value == true {

				err := agentBot.ChatwootClient.Assign(conversationId, 1)

				if err != nil {
					utils.LoggerSugar.Errorf("An error occurred while assigning the conversation to agent 1.", err)
					return err
				}

				agentBot.ChatwootClient.CreateOutgoingMessage(conversationId, agentBot.InteractionCompletedMessage)

			}

		}

		value, ok = customAttributes["human_handoff"]

		if ok {

			if value == true {

				err := agentBot.ChatwootClient.AddLabel(conversationId, "human_handoff")

				if err != nil {
					utils.LoggerSugar.Errorf("An error occurred while adding a label to the conversation.", err)
					return err
				}

				err = agentBot.ChatwootClient.Assign(conversationId, 1)

				if err != nil {
					utils.LoggerSugar.Errorf("An error occurred while assigning the conversation to agent 1.", err)
					return err
				}

				agentBot.ChatwootClient.CreateOutgoingMessage(conversationId, agentBot.InteractionCompletedMessage)

			}
		}

	}

	return nil

}

func (agentBot *ChatwootRasaAgentBot) SendResponseBack(conversationId int, rasaResponse rasa.RasaResponse) error {

	responseText := rasaResponse.GetText()

	responseText = strings.ReplaceAll(responseText, "\\n", "\n")

	if responseText == "" {
		utils.Logger.Info("The response from Rasa is empty. So no new message is created.")
		return nil
	}

	utils.LoggerSugar.Infof("ResponseText: %s", responseText)
	fmt.Println(responseText)

	_, err := agentBot.ChatwootClient.CreateOutgoingMessage(conversationId, responseText)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while making the http request. %s", err)
		return err
	}

	return nil

}
