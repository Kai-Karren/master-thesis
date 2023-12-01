package main

import (
	"github.com/Kai-Karren/chatwoot-golang-client/chatwootclient"
	"github.com/Kai-Karren/cui-gateway/chatwoot"
	"github.com/Kai-Karren/cui-gateway/gateway"
	"github.com/Kai-Karren/cui-gateway/utils"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func main() {

	utils.InitializeLoggers()

	chatwootAgentBotApi := chatwoot.NewChatwootRasaAgentBotPreset(
		chatwootclient.ChatwootClient{
			BaseUrl:       "",
			AccountId:     1,
			AgentToken:    "",
			AgentBotToken: "",
		},
		2,
		"",
		"",
	)

	gatewayAPi := gateway.Gateway{
		ChatwootClient: chatwootclient.ChatwootClient{
			BaseUrl:       "",
			AccountId:     1,
			AgentToken:    "",
			AgentBotToken: "",
		},
		RasaURL:                   "",
		RasaAPIKey:                "",
		InboxID:                   3,
		EnableChatwootIntegration: true,
	}

	router := gin.Default()

	router.Use(cors.New(cors.Config{
		AllowOrigins: []string{"", "", "", "", ""},
		AllowMethods: []string{"GET", "POST"},
		AllowHeaders: []string{"Origin", "Content-Type"},
	}),
	)

	router.POST("/gateway", gatewayAPi.HandleMessages)

	router.POST("/complaint_agent_bot", chatwootAgentBotApi.HandleMessages)

	router.Run(":3011")

}
