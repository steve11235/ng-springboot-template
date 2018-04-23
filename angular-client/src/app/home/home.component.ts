import { Component } from '@angular/core';
import { LoginService } from '../../modules/Login/login.service';
import { AppConfigService } from '../../modules/App/app-config.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent {
  constructor(public appConfig: AppConfigService, public login: LoginService) {}

  handleLogin() {
    this.login.showLogin = true;
  }
}
