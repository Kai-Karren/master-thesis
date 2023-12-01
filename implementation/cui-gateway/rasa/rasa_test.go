package rasa

import (
	"encoding/json"
	"testing"
)

func TestParseRawRasaResponse(t *testing.T) {

	jsonContent := `[
		{
			"recipient_id": "test_user",
			"text": "Vielen Dank für Ihre Meldung. Wir haben Ihre Beschwerde erhalten und werden sie so schnell wie möglich prüfen und bearbeiten. Bitte haben Sie etwas Geduld, während wir uns um Ihr Anliegen kümmern."
		},
		{
			"recipient_id": "test_user",
			"custom": {
				"conversation_completed": true
			}
		}
	]`

	bytes := []byte(jsonContent)

	var rawRasaResponse RawRasaResponse

	if err := json.Unmarshal(bytes, &rawRasaResponse); err != nil {
		t.Fail()
	}

	if len(rawRasaResponse) != 2 {
		t.Fail()
	}

}

func TestParseRasaResponsesWithCustomProperties(t *testing.T) {

	jsonContent := `[
		{
			"recipient_id": "test_user",
			"text": "Vielen Dank für Ihre Meldung.\n\n Wir haben Ihre Beschwerde erhalten und werden sie so schnell wie möglich prüfen und bearbeiten. Bitte haben Sie etwas Geduld, während wir uns um Ihr Anliegen kümmern."
		},
		{
			"recipient_id": "test_user",
			"custom": {
				"conversation_completed": true
			}
		}
	]`

	bytes := []byte(jsonContent)

	var rawRasaResponse RawRasaResponse

	if err := json.Unmarshal(bytes, &rawRasaResponse); err != nil {
		t.Fail()
	}

	rasaResponse := NewRasaResponseFromRaw(rawRasaResponse)

	if rasaResponse.IsEmpty() {
		t.Fail()
	}

	jsonResponse, err := json.Marshal(rasaResponse.GetCustom())

	if err != nil {
		t.Fail()
	}

	if jsonResponse == nil {
		t.Fail()
	}

	if string(jsonResponse) != `{"conversation_completed":true}` {
		t.Fail()
	}

}
