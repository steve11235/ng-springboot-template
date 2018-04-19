import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgZone } from '@angular/core';

import { AppComponent } from './app.component';
import { MessageManagerModule } from '../modules/MessageManager/message-manager.module';
import { ClockModule } from '../modules/Clock/clock.module';
import { HomeModule } from './home/home.module';
import { UsersModule } from './users/users.module';
import { APP_ROUTES } from '../app.routing';
import { LoginModule } from '../modules/Login/login.module';
import { RestCommModule } from '../modules/RestComm/rest-comm.module';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    LoginModule,
    ClockModule,
    MessageManagerModule,
    RestCommModule,
    HomeModule,
    UsersModule,
    APP_ROUTES // must be last
  ],
  providers: [
  ],
  bootstrap: [
    AppComponent
  ]
})

export class AppModule { }
