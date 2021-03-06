import { Component } from '@angular/core';
import { MessageManagerService as MessageManager } from './message-manager.service';
import { Message } from './message';
import { MessageSeverity } from './message-severity';

@Component({
  selector: 'app-message-manager',
  templateUrl: './message-manager.component.html',
  styleUrls: ['./message-manager.component.css']
})
export class MessageManagerComponent {
  messages: Message[] = [];

  constructor(private messageManager: MessageManager) {
    // We need to listen for message changes so this component knows to update
    messageManager.setMessageListener((messages: Message[]) => {
      this.messages = messages;
    });
  }
}
