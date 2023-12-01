import { ActionType } from '@voiceflow/sdk-runtime';
import { useEffect, useState } from 'react';

// Based on https://github.com/voiceflow/react-chat/blob/master/packages/react-chat/src/hooks/useRuntime.ts

import { TurnType, TurnProps, SendMessage, SessionStatus, Message } from '@voiceflow/react-chat';

import { v4 as uuidv4 } from 'uuid';

/**
 * A wrapper for the Voiceflow runtime client.
 */
export const useRuntime = (dialogueEngine: (session: any, message: string, addTurn: (turn: TurnProps) => void, setChatwootConversationId: ((id: number) => void)) => void) => {
  const [indicator, setIndicator] = useState(false);
  const [lastInteractionAt, setLastInteractionAt] = useState<number | null>(Date.now());
  const [noReplyTimeout, setNoReplyTimeout] = useState<number | null>(null);
  const conversation_id = uuidv4();

  const [session, setSession] = useState({
    conversation_id: conversation_id,
    chatwoot_conversation_id: 0,
    status: 'ACTIVE',
    turns: [],
  });

  function addTurn(turn: TurnProps) {

    setSession((prev) => ({ ...prev, turns: [...prev.turns, turn] }));

    sendEvent('chat-turn', {conversation_id: session.conversation_id, chatwoot_conversation_id: session.chatwoot_conversation_id, turn: turn});

  }

  function setChatwootConversationId(id: number) {

    setSession((prev) => ({ ...prev, chatwoot_conversation_id: id }));

  }

  useEffect(() => {
    let noReplyTimer: NodeJS.Timeout | undefined;

    return () => {
      clearTimeout(noReplyTimer);
    };
  }, [noReplyTimeout, lastInteractionAt]);

  const setTurns = (action: (turns: TurnProps[]) => TurnProps[]) => {
    setSession((prev) => ({ ...prev, turns: action(prev.turns) }));
  };

  const setStatus = (status: SessionStatus) => {
    setSession((prev) => ({ ...prev, status }));
  };

  const isStatus = (status: SessionStatus) => {
    return session.status === status;
  };

  const send: SendMessage = async (message: string, action) => {

    addTurn({
      id: uuidv4(),
      type: TurnType.USER,
      message: message,
      timestamp: Date.now(),
    });

    dialogueEngine(session, message, addTurn, setChatwootConversationId)

    if (session.status == SessionStatus.ENDED) return;

  };

  const reset = () => setTurns(() => []);

  const launch = async (): Promise<void> => {

    if (session.turns.length == 0) {

      sendEvent('chat-opened', {});

    }

  };

  const sendEvent = (eventName: string, payload: any) => {
    const event = new CustomEvent(eventName, {
      detail: {
        payload,
      },
    });

    document.dispatchEvent(event);
  };

  const reply = async (message: string): Promise<void> => send(message, { type: ActionType.TEXT, payload: message });

  return {
    send,
    reply,
    reset,
    launch,
    indicator,
    session,
    setStatus,
    isStatus,
    addTurn,
  };
};