import json
from pathlib import Path
from typing import Any, Dict, List, Text

from rasa_sdk import Action, Tracker
from rasa_sdk.events import UserUtteranceReverted, ConversationPaused
from rasa_sdk.executor import CollectingDispatcher

import requests

here = Path(__file__).parent.resolve()

fallbacks = json.load(open(here / "fallbacks.json"))

fallback_memory = {
    "sender_id": {
        "utter_welcome_fallback": 0
    }
}


class ActionDefaultFallback(Action):
    def name(self) -> Text:
        return "action_default_fallback"

    def run(
        self,
        dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any],
    ) -> List[Dict[Text, Any]]:

        lastest_intent = tracker.get_intent_of_latest_message()

        print(lastest_intent)

        print("Last custom action: " + get_last_custom_action(tracker))
        print("Last utterance: " + get_last_utterance(tracker=tracker))

        last_utterance = get_last_utterance(tracker=tracker)

        if last_utterance == "":
            last_utterance = "initial"

        user_fallback_memory = {}

        if tracker.sender_id in fallback_memory.keys():
            user_fallback_memory = fallback_memory[tracker.sender_id]
        else:
            fallback_memory[tracker.sender_id] = user_fallback_memory

        number_of_fallbacks_at_current_state = determine_number_of_triggered_fallbacks_at_current_state(
            user_fallback_memory, last_utterance)

        if number_of_fallbacks_at_current_state > 2:

            print("Stoping")
            dispatcher.utter_message("Es tut mir Leid. Ich bin offenbar nicht in der Lage Ihnen zu helfen."
                                     + " Einer meiner menschlichen Kollegen wird sich bei Ihnen melden. Dies kann einen Moment dauern.", json_message={"human_handoff": True})

            return [ConversationPaused()]

        if isinstance(fallbacks, dict):

            fallback_utterance = last_utterance + "_fallback"

            if fallback_utterance in fallbacks.keys():

                print(fallbacks[fallback_utterance])

                if tracker.sender_id in fallback_memory.keys():

                    if fallback_utterance in user_fallback_memory.keys():

                        fallback_count = user_fallback_memory[fallback_utterance]

                        if fallback_utterance + "_2" in fallbacks.keys():

                            dispatcher.utter_message(
                                text=fallbacks[fallback_utterance + "_2"])

                        else:

                            dispatcher.utter_message(
                                text=fallbacks[fallback_utterance])

                    else:

                        dispatcher.utter_message(
                            text=fallbacks[fallback_utterance])

                        user_fallback_memory[fallback_utterance] = 1

                else:

                    dispatcher.utter_message(
                        text=fallbacks[fallback_utterance])

                    fallback_memory[tracker.sender_id] = {
                        fallback_utterance: 1
                    }

            else:

                dispatcher.utter_message(response="utter_default")

        # pause the tracker so that the bot stops responding to user input
        return [UserUtteranceReverted()]

    def reset_fallback_memory(self):
        global fallback_memory
        fallback_memory = {}

    def reset_fallback_memory_for_sender(self, sender_id):
        global fallback_memory
        fallback_memory[sender_id] = {}


def determine_number_of_triggered_fallbacks_at_current_state(user_fallback_memory, last_utterance):

    if last_utterance not in user_fallback_memory.keys():

        user_fallback_memory[last_utterance] = 1

        return 1

    else:

        count = user_fallback_memory[last_utterance]

        count += 1

        user_fallback_memory[last_utterance] = count

        return count


def generate_fallback_response_with_chatgpt(url: str, tracker: Tracker):

    conversation_history = construct_conversation_history_from_events(
        tracker.events)

    request_body = {
        "model": "gpt-3.5-turbo",
        "messages": [
            {
                "role": "user",
                "content": "Generate a fallback response for a chatbot in German with the following conversation history."
                + json.dumps(conversation_history)
            }
        ]
    }

    response = requests.post(url=url,
                             headers={
                                 "Authorization": ""},
                             json=request_body)

    response_object = response.json()

    print(response_object)

    return response_object["choices"][0]["message"]["content"]


# Method that returns the name of the last utterance that appeared in the conversation.
def get_last_utterance(tracker: Tracker):

    for event in reversed(tracker.events):

        if event["event"] == "action":
            if "utter" in event["name"]:
                print(event["name"])
                return event["name"]

    return ""


def get_last_custom_action(tracker: Tracker):
    '''Method that returns the name of the last custom action that has been executed'''

    for event in reversed(tracker.events):

        if event["event"] == "action":

            action_name = event["name"]

            if "action" in action_name:

                if is_custom_action(action_name=action_name):
                    return event["name"]

    return ""


def is_custom_action(action_name):

    rasa_default_actions = [
        "action_listen", "action_default_fallback", "action_unlikely_intent", "action_restart", "action_back",
        "action_extract_slots", "action_default_ask_affirmation", "action_session_start",
        "action_default_ask_rephrase", "action_two_stage_fallback", "action_deactivate_loop"
    ]

    if action_name in rasa_default_actions:
        return False
    else:
        return True


def construct_conversation_history_from_events(events: List):

    conversation_history = []

    for event in events:

        event_name = event["event"]

        if event_name == "bot":
            conversation_history.append({"system": event["text"]})
        elif event_name == "user":
            conversation_history.append({"user": event["text"]})

    return conversation_history


class ActionGetFeedback(Action):
    def name(self) -> Text:
        return "action_get_feedback"

    def run(
        self,
        dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any],
    ) -> List[Dict[Text, Any]]:

        url = '<URL>'

        request_object = {
            "user": tracker.sender_id,
            "location": {
                "street": tracker.slots["street"],
                # "number": tracker.slots["number"],
                # "point_of_interest": tracker.slots["point_of_interest"]
            },
            "category": determine_complaint_category(tracker),
            # "reason": tracker.slots["reason"],
        }

        print(request_object)

        response = requests.post(
            url, json=request_object, headers={"apikey": ""})

        feedback_response = response.json()

        print(feedback_response)

        dispatcher.utter_message(text=feedback_response["text"], json_message={
                                 "conversation_completed": True})

        return [ConversationPaused()]


def determine_complaint_category(tracker: Tracker):

    for event in tracker.events:

        if event["event"] == "user":

            parse_data = event["parse_data"]

            intent = parse_data["intent"]

            if "complaint" in intent["name"]:

                if intent["name"] == "general_complaint":

                    return "pollution"

                return intent["name"].replace("_complaint", "")

            if "category" in intent["name"]:

                return intent["name"].replace("_category")

    return "other"


def list_conversation_intents(tracker: Tracker):

    intents = []

    for event in tracker.events:

        if event["event"] == "user":

            parse_data = event["parse_data"]

            intent = parse_data["intent"]

            intents.append(intent)

    return intents
