package chatwoot

import (
	"bytes"
	"fmt"
	"io"
	"net/http"

	"github.com/Kai-Karren/cui-gateway/utils"
	"github.com/gin-gonic/gin"
)

type EchoAgentBot struct {
	OutgoingUrl string
	Token       string
}

func NewEchoAgentBot(outgoingUrl string, token string) EchoAgentBot {

	return EchoAgentBot{
		OutgoingUrl: outgoingUrl,
		Token:       token,
	}

}

func (api *EchoAgentBot) HandleMessages(c *gin.Context) {

	var request Event

	if err := c.BindJSON(&request); err != nil {
		utils.LoggerSugar.Errorf("An error occurred while handling the event from chatwoot %s", err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"error": "500", "message": err.Error()})
		return
	}

	utils.LoggerSugar.Infof("Received the request: %v", request)

	if request.Event == "message_created" && request.MessageType == "incoming" {

		utils.LoggerSugar.Infof("Received: %v", request.Content)

		responseFromBot := api.GetResponseFromBot(request.Sender.ID, request.Content)

		api.SendResponseBack(request.Account.ID, request.Conversation.ID, responseFromBot)

	}

}

func (api *EchoAgentBot) GetResponseFromBot(sender int, message string) string {
	return message
}

func (api *EchoAgentBot) SendResponseBack(account int, conversation int, message string) {

	/*
		{
		  "content": "Example content of a user message",
		  "message_type": "incoming",
		  "private": false
		}
	*/

	content := fmt.Sprintf(`{"content": "%s", "message_type": "outgoing", "private": false}`, message)

	jsonBody := []byte(content)

	bodyReader := bytes.NewReader(jsonBody)

	url := fmt.Sprintf(api.OutgoingUrl+"/api/v1/accounts/%v/conversations/%v/messages", account, conversation)

	request, err := http.NewRequest(http.MethodPost, url, bodyReader)

	request.Header.Set("Content-Type", "application/json")
	request.Header.Set("Accept", "application/json")
	request.Header.Set("api_access_token", api.Token)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while creating the http request. %s", err)
		return
	}

	result, err := http.DefaultClient.Do(request)

	if err != nil {
		utils.LoggerSugar.Errorf("An error occurred while making the http request. %s", err)
		return
	}

	resultBody, err := io.ReadAll(result.Body)
	if err != nil {
		utils.LoggerSugar.Errorf("client: could not read response body: %s", err)
		return
	}

	utils.LoggerSugar.Infof("Received response body: %s", resultBody)

}
