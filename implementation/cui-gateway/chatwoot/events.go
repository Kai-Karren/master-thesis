package chatwoot

import "time"

const (
	ConversationCreated      string = "conversation_created"
	ConversationUpdated      string = "conversation_updated"
	ConversationStatusChange string = "conversation_status_change"
	MessageCreated           string = "message_created"
	MessageUpdated           string = "message_updated"
	WebWidgetTriggered       string = "webwidget_triggered"
)

const (
	Id       string = "id"
	Incoming string = "incoming"
	Outgoing string = "outgoing"
)

// Documentation can be found here https://www.chatwoot.com/docs/product/others/webhook-events
// All key value pairs that are expected to be used could be put in this struct.
type Event struct {
	Event       string `json:"event"`
	SourceID    string `json:"source_id"`
	ID          int    `json:"id"`
	MessageType string `json:"message_type"`
	Content     string `json:"content"`
	// CreatedAt           int    `json:"created_at"` // Dropped the createdAt propertey because of the time parsing bug.
	CurrentConversation struct {
		ID int `json:"id"`
	} `json:"current_conversation"`
	Conversation struct {
		ID int `json:"id"`
	} `json:"conversation"`
	Account struct {
		ID int `json:"id"`
	} `json:"account"`
	Sender struct {
		ID int `json:"id"`
	} `json:"sender"`
}

// Created with https://mholt.github.io/json-to-go/
type MessageCreatedEvent struct {
	Account struct {
		ID   int    `json:"id"`
		Name string `json:"name"`
	} `json:"account"`
	AdditionalAttributes struct {
	} `json:"additional_attributes"`
	ContentAttributes struct {
	} `json:"content_attributes"`
	ContentType  string `json:"content_type"`
	Content      string `json:"content"`
	Conversation struct {
		AdditionalAttributes struct {
		} `json:"additional_attributes"`
		CanReply     bool   `json:"can_reply"`
		Channel      string `json:"channel"`
		ContactInbox struct {
			ID           int       `json:"id"`
			ContactID    int       `json:"contact_id"`
			InboxID      int       `json:"inbox_id"`
			SourceID     string    `json:"source_id"`
			CreatedAt    time.Time `json:"created_at"`
			UpdatedAt    time.Time `json:"updated_at"`
			HmacVerified bool      `json:"hmac_verified"`
			PubsubToken  string    `json:"pubsub_token"`
		} `json:"contact_inbox"`
		ID       int `json:"id"`
		InboxID  int `json:"inbox_id"`
		Messages []struct {
			ID                int         `json:"id"`
			Content           string      `json:"content"`
			AccountID         int         `json:"account_id"`
			InboxID           int         `json:"inbox_id"`
			ConversationID    int         `json:"conversation_id"`
			MessageType       int         `json:"message_type"`
			CreatedAt         int         `json:"created_at"`
			UpdatedAt         time.Time   `json:"updated_at"`
			Private           bool        `json:"private"`
			Status            string      `json:"status"`
			SourceID          interface{} `json:"source_id"`
			ContentType       string      `json:"content_type"`
			ContentAttributes struct {
			} `json:"content_attributes"`
			SenderType        string `json:"sender_type"`
			SenderID          int    `json:"sender_id"`
			ExternalSourceIds struct {
			} `json:"external_source_ids"`
			AdditionalAttributes struct {
			} `json:"additional_attributes"`
			Conversation struct {
				AssigneeID int `json:"assignee_id"`
			} `json:"conversation"`
			Sender struct {
				AdditionalAttributes struct {
				} `json:"additional_attributes"`
				CustomAttributes struct {
				} `json:"custom_attributes"`
				Email       string      `json:"email"`
				ID          int         `json:"id"`
				Identifier  string      `json:"identifier"`
				Name        string      `json:"name"`
				PhoneNumber interface{} `json:"phone_number"`
				Thumbnail   string      `json:"thumbnail"`
				Type        string      `json:"type"`
			} `json:"sender"`
		} `json:"messages"`
		Meta struct {
			Sender struct {
				AdditionalAttributes struct {
				} `json:"additional_attributes"`
				CustomAttributes struct {
				} `json:"custom_attributes"`
				Email       string      `json:"email"`
				ID          int         `json:"id"`
				Identifier  string      `json:"identifier"`
				Name        string      `json:"name"`
				PhoneNumber interface{} `json:"phone_number"`
				Thumbnail   string      `json:"thumbnail"`
				Type        string      `json:"type"`
			} `json:"sender"`
			Assignee struct {
				ID                 int         `json:"id"`
				Name               string      `json:"name"`
				AvailableName      string      `json:"available_name"`
				AvatarURL          string      `json:"avatar_url"`
				Type               string      `json:"type"`
				AvailabilityStatus interface{} `json:"availability_status"`
				Thumbnail          string      `json:"thumbnail"`
			} `json:"assignee"`
			HmacVerified bool `json:"hmac_verified"`
		} `json:"meta"`
		Status           string `json:"status"`
		CustomAttributes struct {
		} `json:"custom_attributes"`
		SnoozedUntil      interface{} `json:"snoozed_until"`
		UnreadCount       int         `json:"unread_count"`
		AgentLastSeenAt   int         `json:"agent_last_seen_at"`
		ContactLastSeenAt int         `json:"contact_last_seen_at"`
		Timestamp         int         `json:"timestamp"`
	} `json:"conversation"`
	CreatedAt time.Time `json:"created_at"`
	ID        int       `json:"id"`
	Inbox     struct {
		ID   int    `json:"id"`
		Name string `json:"name"`
	} `json:"inbox"`
	MessageType string `json:"message_type"`
	Private     bool   `json:"private"`
	Sender      struct {
		ID      int    `json:"id"`
		Name    string `json:"name"`
		Avatar  string `json:"avatar"`
		Type    string `json:"type"`
		Account struct {
			ID   int    `json:"id"`
			Name string `json:"name"`
		} `json:"account"`
	} `json:"sender"`
	SourceID interface{} `json:"source_id"`
	Event    string      `json:"event"`
}
