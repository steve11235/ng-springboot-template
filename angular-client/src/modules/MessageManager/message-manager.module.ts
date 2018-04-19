import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MessageManagerComponent } from './message-manager.component';
import { MessageManager } from './message-manager.service';

@NgModule({
  declarations: [
    MessageManagerComponent
  ],
  imports: [
    CommonModule
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
