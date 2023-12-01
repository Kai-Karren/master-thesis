export const json = {
    "title": "Bürgerbeschwerde Assistent Umfrage",
    "pages": [
        {
            "name": "general_page",
            "elements": [
                {
                    "type": "panel",
                    "name": "general-panel",
                    "elements": [
                        {
                            "type": "html",
                            "name": "attrakdiff-example-text",
                            "html": "<p>Im folgenden werden Ihnen einige Fragen gestellt. Bitte beantworten Sie diese so ehrlich wie möglich. Verpflichtend sind nur die Fragen, die mit einem Sternchen (*) gekennzeichnet sind. Die Umfrage ist anonym und die Daten werden nur für wissenschaftliche Zwecke verwendet.</p>"
                        },
                        {
                            "type": "boolean",
                            "name": "lives_in_wadgassen",
                            "title": "Wohnen Sie in Wadgassen?",
                            "valueTrue": "Ja",
                            "valueFalse": "Nein",
                            "renderAs": "radio"
                        },
                        {
                            "name": "age",
                            "type": "text",
                            "title": "Wie alt sind Sie?",
                            "inputType": "number",
                            "min": 0,
                            "max": 100,
                        },
                        {
                            "type": "radiogroup",
                            "name": "gender",
                            "title": "Mit welchem Geschlecht identifizieren Sie sich?",
                            "colCount": 1,
                            "choices": ["männlich", "weiblich", "divers", "keine Angabe"],
                        },
                        {
                            "type": "rating",
                            "name": "chatbot_experience",
                            "title": "Wie oft haben Sie bisher Chatbots benutzt?",
                            "minRateDescription": "gar nicht",
                            "maxRateDescription": "sehr viel",
                            "displayMode": "buttons",
                            "isRequired": true
                        }

                    ]
                }
            ]
        },
        {
            "name": "page1",
            "elements": [
                {
                    "type": "panel",
                    "name": "cuq-panel",
                    "elements": [
                        {
                            "type": "rating",
                            "name": "cuq_personality",
                            "title": "Die Persönlichkeit des Chatbots war realistisch und ansprechend.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_robotic",
                            "title": "Der Chatbot wirkte zu roboterhaft.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_welcoming",
                            "title": "Der Chatbot war freundlich.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_unfriendly",
                            "title": "Der Chatbot wirkte sehr unfreundlich.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_scope",
                            "title": "Der Chatbot erklärte seinen Umfang und Zweck gut.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_no_indication",
                            "title": "Der Chatbot gab keine Hinweise auf seinen Zweck.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_easy_navigation",
                            "title": "Der Chatbot war leicht zu navigieren.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_confusing",
                            "title": "Es wäre leicht, sich bei der Verwendung des Chatbots zu vertun.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_understanding",
                            "title": "Der Chatbot verstand mich gut.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_not_recognized",
                            "title": "Der Chatbot konnte viele meiner Eingaben nicht erkennen.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_relevant_and_useful_responses",
                            "title": "Die Antworten des Chatbots waren nützlich, angemessen und informativ.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_irrelevant",
                            "title": "Die Antworten des Chatbots waren irrelevant.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_coped_errors",
                            "title": "Der Chatbot kam gut mit Fehlern zurecht.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_unable_to_cope_errors",
                            "title": "Der Chatbot schien nicht in der Lage zu sein, Fehler zu behandeln.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_easy_to_use",
                            "title": "Der Chatbot war sehr einfach zu bedienen.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "cuq_complexity",
                            "title": "Der Chatbot war sehr komplex.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        }

                    ]
                },

            ]
        },
        {
            "name": "page2",
            "elements": [
                {
                    "type": "panel",
                    "name": "attrackdiff-panel",
                    "elements": [
                        {
                            "type": "html",
                            "name": "attrakdiff-example-text",
                            "html": "<p>Nachfolgend finden Sie Wortpaare, mit deren Hilfe Sie die Beurteilung des Bürgerbeschwerde Assistenten vornehmen können. Sie stellen jeweils extreme Gegensätze dar, zwischen denen eine Abstufung möglich ist.<br></p> <p>Ein Beispiel:</p>"
                        },
                        {
                            "type": "image",
                            "name": "attrakdiff-example-image",
                            "imageLink": "../img/attrakDiff_example_screenshot.png",
                            "imageHeight": 'auto',
                            "imageWidth": 'auto',
                            "altText": "Beispiel AttrakDiff Item",
                        },
                        {
                            "type": "html",
                            "name": "attrakdiff-example-text",
                            "html": "<p>Diese Bewertung bedeutet, dass das Produkt eher als einfach wahrgenommen wird, aber noch verbesserungsbedürftig ist.<br><br> Denken Sie nicht lange über die Wortpaare nach, sondern geben Sie bitte die Einschätzung ab, die Ihnen spontan in den Sinn kommt. Vielleicht passen einige Wortpaare nicht so gut auf das Produkt, kreuzen Sie aber trotzdem bitte immer eine Antwort an. Denken Sie daran, dass es keine \"richtigen\" oder \"falschen\" Antworten gibt - nur Ihre persönliche Meinung zählt!</p>"
                        },
                        {
                            "type": "html",
                            "name": "attrakdiff-example-text",
                            "html": "<p>Bitte geben Sie mit Hilfe der folgenden Wortpaare Ihren Eindruck zum Bürgerbeschwerde Assistenten wieder.</p>"
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_01",
                            "title": "einfach - kompliziert",
                            "minRateDescription": "einfach",
                            "maxRateDescription": "kompliziert",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_02",
                            "title": "hässlich - schön",
                            "minRateDescription": "hässlich",
                            "maxRateDescription": "schön",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_03",
                            "title": "praktisch - unpraktisch",
                            "minRateDescription": "praktisch",
                            "maxRateDescription": "unpraktisch",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_04",
                            "title": "stilvoll - stillos",
                            "minRateDescription": "stilvoll",
                            "maxRateDescription": "stillos",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_05",
                            "title": "voraussagbar - unberechenbar",
                            "minRateDescription": "voraussagbar",
                            "maxRateDescription": "unberechenbar",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_06",
                            "title": "minderwertig - wertvoll",
                            "minRateDescription": "minderwertig",
                            "maxRateDescription": "wertvoll",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_07",
                            "title": "phantasielos - kreativ",
                            "minRateDescription": "phantasielos",
                            "maxRateDescription": "kreativ",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_08",
                            "title": "gut - schlecht",
                            "minRateDescription": "gut",
                            "maxRateDescription": "schlecht",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_09",
                            "title": "verwirrend - übersichtlich",
                            "minRateDescription": "verwirrend",
                            "maxRateDescription": "übersichtlich",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "ad_s_10",
                            "title": "lahm - fesselnd",
                            "minRateDescription": "lahm",
                            "maxRateDescription": "fesselnd",
                            "displayMode": "buttons",
                            "rateValues": [1, 2, 3, 4, 5, 6, 7],
                            "isRequired": true
                        }
                    ]
                }
            ]
        },
        {
            "name": "page_trust",
            "elements": [
                {
                    "type": "panel",
                    "name": "trust-panel",
                    "description": "",
                    "elements": [
                        {
                            "type": "rating",
                            "name": "trust_1",
                            "title": "Ich vertraue meiner Gemeinde nicht.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "trust_2",
                            "title": "Ich glaube, dass meine Gemeinde mich nicht täuschen wurde.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "trust_3",
                            "title": "Ich habe das Gefühl, dass ich meiner Gemeinde vollkommen vertrauen kann.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "trust_4",
                            "title": "Ich fühle mich sicher, wenn ich mit Dienstleistungen/Anwendungen interagiere, die von meiner Gemeinde bereitgestellt wurden",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "trust_5",
                            "title": "Ich glaube, ich kann mich darauf verlassen, dass meine Gemeinde effektive und effiziente Dienstleistungen/Anwendungen bereitstellt.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        }
                    ]
                }
            ]
        },
        {
            "name": "page3",
            "elements": [
                {
                    "type": "panel",
                    "name": "custom-specific-panel",
                    "elements": [
                        {
                            "type": "rating",
                            "name": "custom_positive_effect_of_ai",
                            "title": "Künstliche Intelligenz wird einen positiven Effekt auf die Gesellschaft haben.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "boolean",
                            "name": "custom_experience_with_llms",
                            "title": "Haben Sie schon mal ein \"Large Language Model (LLM)\" wie z. B. ChatGPT benutzt?",
                            "valueTrue": "Yes",
                            "valueFalse": "No",
                            "renderAs": "radio",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "attention_question",
                            "title": "Um Ihre Aufmerksamkeit zu testen, wählen Sie bitte 1 aus.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "custom_my_municipality_takes_complaints_seriously",
                            "title": "Meine Stadt/Gemeinde nimmt meine Beschwerden ernst.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "custom_complaint_assistance_willingness_to_use_it",
                            "title": "Wenn meine Stadt/Gemeinde einen solchen Chatbot anbieten würde, würde ich ihn nutzen.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "custom_human_intervention_option",
                            "title": "Würden Sie sich wohler fühlen mit dem Chatbot, wenn Sie wüssten, dass menschliche Mitarbeiter die Konversationen beobachten und bei Bedarf eingreifen können?",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "comment",
                            "name": "custom_desired_information",
                            "title": "Gibt es Informationen, die Sie gerne vom Chatbot bezüglich Ihrer Beschwerde gewünscht hätten?",
                            "rows": 2,
                            "autoGrow": true,
                            "allowResize": false
                        },
                        {
                            "type": "comment",
                            "name": "custom_unwanted_information",
                            "title": "Haben Sie Informationen bekommen, die Sie nicht hilfreich fanden? Wenn ja, welche waren das?",
                            "rows": 2,
                            "autoGrow": true,
                            "allowResize": false
                        },
                        {
                            "type": "radiogroup",
                            "name": "custom_preferred_interaction_channel",
                            "title": "Welche der folgenden Möglichkeiten zur Interaktion mit dem Chatbot wäre Ihre Präferenz?",
                            "description": "",
                            "choices": ["WhatsApp", "Facebook Messenger", "Telegram", "Gemeinde Website"],
                            "isRequired": true,
                            "colCount": 2,
                            "showOtherItem": true,
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "custom_progress_updates",
                            "title": "Updates über Fortschritte bei der Lösung meiner Beschwerde wären mir wichtig.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "rating",
                            "name": "custom_resolution_notification",
                            "title": "Ich möchte informiert werden, wenn meine Beschwerde gelöst wurde.",
                            "minRateDescription": "Stimme überhaupt nicht zu",
                            "maxRateDescription": "Stimme voll und ganz zu",
                            "displayMode": "buttons",
                            "isRequired": true
                        },
                        {
                            "type": "comment",
                            "name": "custom_additional_comments",
                            "title": "Möchten Sie sonst noch etwas ergänzen?",
                            "rows": 2,
                            "autoGrow": true,
                            "allowResize": false
                        },

                    ]
                }
            ]
        },
    ],
    "showPrevButton": false,
    "showQuestionNumbers": "off",
    "completeText": {
        "fr": "Envoyer"
    },
    "questionsOnPageMode": "questionPerPage",
    "showProgressBar": "bottom",
    "showCompletedPage": true,
    "completedHtml": "<p>Vielen Dank Ihre Teilnahme ist nun abgeschlossen.</p> </br> <p>Für Nutzer von SurveyCircle (www.surveycircle.com): Der Survey Code lautet: ESB3-DY53-M8XM-9PAN</p>",
    "widthMode": "static",
    "width": "1250px"
};