import { Component } from '@angular/core';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage = '';

  constructor(public loginService: LoginService) {}

  handleCancel(): void {
    this.loginService.showLogin = false;
  }
}
