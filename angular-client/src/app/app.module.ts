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
import { AppConfigService } from '../modules/App/app-config.service';
import { APP_CONFIG_SERVICE } from './app-config-service-instance';
@NgModule({
  declarations: [AppComponent],
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
  providers: [{provide: AppConfigService, useValue: APP_CONFIG_SERVICE}],
  bootstrap: [AppComponent]
})
export class AppModule {}
