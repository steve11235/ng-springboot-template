import { Component } from "@angular/core";
import { LoginService } from "../../modules/Login/login.service";

@Component(
    {
        selector: "home",
        templateUrl: "./home.component.html"
    }
)
export class HomeComponent {

    constructor(public loginService: LoginService) {}

    handleLogin() {
        this.loginService.showLogin = true;
      }
}