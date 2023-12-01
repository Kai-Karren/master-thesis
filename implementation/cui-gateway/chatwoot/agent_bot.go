package chatwoot

import "github.com/gin-gonic/gin"

type AgentBot interface {
	HandleMessages(c *gin.Context)
}
