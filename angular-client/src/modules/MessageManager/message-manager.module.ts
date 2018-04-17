import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MessageManagerComponent } from './message-manager.component';
import { MessageManager } from './message-manager.service';

@NgModule({
  declarations: [
    MessageManagerComponent
  ],
  imports: [
    BrowserModule
  ],
  exports: [
    MessageManagerComponent
  ],
  providers: [
    MessageManager
  ],
  bootstrap: [
    MessageManagerComponent
  ]
})
export class MessageManagerModule { }
