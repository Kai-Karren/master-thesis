package rasa

type RawRasaResponse []struct {
	Text   string                 `json:"text"`
	Image  string                 `json:"image"`
	Custom map[string]interface{} `json:"custom"`
}

type RasaResponse struct {
	Messages []RasaMessage
}

func NewRasaResponseSingleMessage(text string) RasaResponse {
	return RasaResponse{
		Messages: []RasaMessage{{
			Text: text,
		}},
	}
}

type RasaMessage struct {
	Text   string                 `json:"text"`
	Image  string                 `json:"image"`
	Custom map[string]interface{} `json:"custom"`
}

func NewRasaResponseFromRaw(rawRasaResponse RawRasaResponse) RasaResponse {

	messages := []RasaMessage{}

	if rawRasaResponse == nil {
		return RasaResponse{
			Messages: messages,
		}
	}

	for _, rawMessage := range rawRasaResponse {

		messages = append(messages, RasaMessage{
			Text:   rawMessage.Text,
			Image:  rawMessage.Image,
			Custom: rawMessage.Custom,
		})

	}

	return RasaResponse{
		Messages: messages,
	}

}

func (response *RasaResponse) IsEmpty() bool {
	return len(response.Messages) == 0
}

func (response *RasaResponse) GetText() string {

	// There could be multiple text messages in a single response object, so they are concatinated here.
	text := ""

	for _, message := range response.Messages {

		text += message.Text

	}

	return text

}

func (response *RasaResponse) HasCustom() bool {

	for _, message := range response.Messages {

		if message.Custom != nil {
			return true
		}

	}

	return false

}

func (response *RasaResponse) GetCustom() map[string]interface{} {

	for _, message := range response.Messages {

		if message.Custom != nil {
			return message.Custom
		}

	}

	return nil

}
