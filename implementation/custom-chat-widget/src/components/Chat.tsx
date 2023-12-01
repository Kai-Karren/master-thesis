import { Chat, ChatWindow, Launcher, SessionStatus, SystemResponse, TurnType, UserResponse, TurnProps } from '@voiceflow/react-chat';
import { useState } from 'react';
import { useRuntime } from './hooks';

import { v4 as uuidv4 } from 'uuid';

const IMAGE = './BotAvatar.svg';
const AVATAR = './BotAvatar.svg';

const MyChat: React.FC = () => {

  const [open, setOpen] = useState(false);

  const mockDe = async (session: any, message: string, addTurn: (turn: TurnProps) => void, setChatwootConversationId: ((id: number) => void)) => {

    console.log("mockDe", session.conversation_id, message)

    addTurn({
      id: uuidv4(),
      type: TurnType.SYSTEM,
      timestamp: Date.now(),
      messages: [
        {
          type: "text",
          text: message,
        },
      ],
    });

  }

  const onlineMock = async (session: any, message: string, addTurn: (turn: TurnProps) => void, setChatwootConversationId: ((id: number) => void)) => {

    const response = await fetch("", {
      method: "POST",
      body: JSON.stringify({
        conversation_id: session.conversation_id,
        message: message
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8"
      }
    });

    const json = await response.json();

    console.log("onlineMock", json)

    addTurn({
      id: uuidv4(),
      type: TurnType.SYSTEM,
      timestamp: Date.now(),
      messages: [
        {
          type: "text",
          text: json["text"],
        },
      ],
    });

  }

  const parseEscapedLineBreaks = (text: string) => {
    return text.replace(/\\n/g, "\n");
  }

  const cuiGateway = async (session: any, message: string, addTurn: (turn: TurnProps) => void, setChatwootConversationId: ((id: number) => void)) => {

    try {

      // const url = "http://localhost:3011/gateway";
      const url = "";

      const response = await fetch(url, {
        method: "POST",
        body: JSON.stringify({
          conversation_id: session.conversation_id,
          chatwoot_conversation_id: session.chatwoot_conversation_id,
          text: message
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8"
        }
      });

      const json = await response.json();

      const messages = json["messages"];

      if (session.chatwoot_conversation_id == 0) {

        const chatwoot_conversation_id_in_response = json["chatwoot_conversation_id"];

        if (chatwoot_conversation_id_in_response != 0) {

          setChatwootConversationId(chatwoot_conversation_id_in_response);

          const event = new CustomEvent("chatwoot-conversation-id", {
            detail: {
              chatwoot_conversation_id: chatwoot_conversation_id_in_response,
            },
          });

          document.dispatchEvent(event);
        }

      }


      const internalMessages = [];

      messages.forEach(message => {

        if (message["type"] == "text") {

          const text = message["text"];

          if (text == "") {
            return;
          }

          const internalMessage = {
            type: "text",
            text: parseEscapedLineBreaks(text),
          }

          internalMessages.push(internalMessage);

        } else if (message["type"] == "custom") {

          const event = new CustomEvent("custom-bot-response", {
            detail: {
              custom: message["custom"],
              chatwoot_conversation_id: session.chatwoot_conversation_id,
            },
          });

          document.dispatchEvent(event);

          internalMessages.push({
            type: "text",
            text: "Sie haben das Ende Ihrer Interaktion mit dem System im Rahmen dieser Studie erreicht. Sie können den Chat nun schließen und auf den Link zum Fragebogen klicken. Der Fragebogen wird sich in einem eigenen Tab öffnen.",
          });

        }

      });

      addTurn({
        id: uuidv4(),
        type: TurnType.SYSTEM,
        timestamp: Date.now(),
        messages: internalMessages,

      });

    } catch (e) {

      addTurn({
        id: uuidv4(),
        type: TurnType.SYSTEM,
        timestamp: Date.now(),
        messages: [
          {
            type: "text",
            text: "Es ist ein Fehler aufgetreten. Ich habe meine menschlichen Kollegen informiert und sie werden sich so schnell wie möglich darum kümmern.",
          }
        ],
      });

    }

  }

  const runtime = useRuntime(cuiGateway)

  const handleLaunch = async () => {
    setOpen(true);
    await runtime.launch();
  };

  document.addEventListener('chat-opened', (e) => {

    runtime.addTurn({
      id: uuidv4(),
      type: TurnType.SYSTEM,
      timestamp: Date.now(),
      messages: [
        {
          type: "text",
          text: "Hallo, ich bin der Bürger Beschwerde Assistent der Gemeinde Wadgassen. Meine Aufgabe ist es, Bürger beim Melden von Problemen zu unterstützen. Lassen Sie mich Ihnen zunächst danken, da jede Beschwerde uns hilft Feedback zu sammeln und Probleme zu erkennen und zu lösen. Bitte beschreiben Sie worüber Sie sich beschweren möchten.",
        },
      ],
    });

  });

  const handleEnd = () => {
    runtime.setStatus(SessionStatus.ENDED);
    setOpen(false);
  };

  if (!open) {
    return (
      <span
        style={{
          position: 'fixed',
          bottom: '0px',
          right: '0px',
          margin: '1em',
        }}
      >
        <Launcher onClick={handleLaunch} />
      </span>
    );
  }

  return (
    <div
      style={{
        position: 'fixed',
        width: '400px',
        height: '600px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        overflowX: 'hidden',
        bottom: '0px',
        right: '0px',
        margin: '1em',
      }}
    >
      <ChatWindow.Container>
        <Chat
          title="Bürgerbeschwerde Assistent"
          image={IMAGE}
          avatar={AVATAR}
          isLoading={!runtime.session.turns.length}
          onStart={runtime.launch}
          onEnd={handleEnd}
          onSend={runtime.reply}
          onMinimize={handleEnd}
        >
          {
            runtime.session.turns.map((turn, turnIndex) => {
              if (turn.type === TurnType.USER) {
                return <UserResponse {...turn} key={turn.id} />
              } else if (turn.type === TurnType.SYSTEM) {
                return <SystemResponse {...turn} key={turn.id} avatar={AVATAR} />
              } else {
                return <SystemResponse.Indicator avatar={AVATAR} />
              }
            },)
          }

        </Chat>
      </ChatWindow.Container>
    </div>
  );
};

export default MyChat;