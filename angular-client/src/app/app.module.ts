import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgZone } from '@angular/core';

import { AppComponent } from './app.component';
import { MessageManagerModule } from '../modules/MessageManager/message-manager.module';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    MessageManagerModule
  ],
  providers: [
  ],
  bootstrap: [
    AppComponent
  ]
})

export class AppModule { }
