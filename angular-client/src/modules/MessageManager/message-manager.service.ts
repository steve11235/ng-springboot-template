import { Injectable } from '@angular/core';
import { Message } from './message';
import { MessageSeverity } from './message-severity';
import { populateClassInstanceFromDto } from '../Utilities/utilityMethods';

@Injectable()
export class MessageManagerService {
  private messages: Message[] = [];
  private messageListener: (messages: Message[]) => void;

  /**
   * Set a listener for message changes. This method is called by MessageManagerComponent ONLY.
   * @param listener
   */
  setMessageListener(listener: (messages: Message[]) => void) {
    this.messageListener = listener;
  }

  clearMessages(): void {
    this.messages = [];

    this.alertListener();
  }

  addMessage(text: string, severity: MessageSeverity, entity: string = null, entityKey: number = null, entityField: string = null) {
    const message: Message = new Message();
    message.text = text;
    message.severity = severity;
    message.entity = entity;
    message.entityKey = entityKey;
    message.entityField = entityField;
    message.seal();

    this.messages.push(message);

    this.alertListener();
  }

  addMessages(rawMessages: any[]): void {
    if (!rawMessages || !rawMessages.length) {
      return;
    }

    const newMessages: Message[] = rawMessages
      .map(rawMessage => {
        const message: Message = new Message();
        populateClassInstanceFromDto(message, rawMessage);

        return message;
      })
      .filter(message => {
        return message.text && message.severity;
      });

    this.messages = this.messages.concat(newMessages);

    this.alertListener();
  }

  /**
   * Return all entity Messages; i.e., those with entity, etc., defined. This simplifies correlating messages to template fields.
   */
  getEntityMessages(): Message[] {
    const entityMessages: Message[] = this.messages.filter(message => {
      return message.entity && message.entityKey && message.entityField;
    });

    return entityMessages;
  }

  private alertListener(): void {
      if (!this.messageListener) {
          return;
      }

      this.messageListener(this.messages);
  }
}
