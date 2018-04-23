import { Injectable } from '@angular/core';

@Injectable()
export class LoginService {
  loggedIn = false;
  showLogin = false;
  jwt: string = null;
}
