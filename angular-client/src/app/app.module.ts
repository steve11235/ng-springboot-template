import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgZone } from '@angular/core';

import { AppComponent } from './app.component';
import { MessageManagerModule } from '../modules/MessageManager/message-manager.module';
import { ClockModule } from '../modules/Clock/clock.module';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    MessageManagerModule,
    ClockModule
  ],
  providers: [
  ],
  bootstrap: [
    AppComponent
  ]
})

export class AppModule { }
