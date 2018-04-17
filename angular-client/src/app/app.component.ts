import { Component, OnInit } from '@angular/core';

import { MessageManagerComponent } from '../modules/MessageManager/message-manager.component'
import { MessageManager } from '../modules/MessageManager/message-manager.service';
import { MessageSeverity } from '../modules/MessageManager/message-severity';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private messageManager: MessageManager) {  }

  ngOnInit() {
    this.messageManager.addMessage("Not sure if MessageManager is working...", MessageSeverity.WARN);
    this.messageManager.addMessage("Woo Hoo! It works!", MessageSeverity.INFO);
    this.messageManager.addMessage("Woo Hoo! It works!", MessageSeverity.INFO);
    this.messageManager.addMessage("Woo Hoo! It works!!", MessageSeverity.INFO);
    this.messageManager.addMessage("Woo Hoo! It works!!!", MessageSeverity.INFO);
    this.messageManager.addMessage("Woo Hoo! It works!!!!", MessageSeverity.INFO);
  }
}
