import { Injectable } from "@angular/core";

@Injectable()
export class LoginService {
    loggedIn: boolean = false;
    showLogin: boolean = false;
};