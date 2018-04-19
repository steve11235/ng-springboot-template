import { Component } from "@angular/core";
import { LoginService } from "./login.service";

@Component({
  selector: "login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"]
})
export class LoginComponent {
    errorMessage: string = "";

    constructor(public loginService: LoginService) {}

    handleCancel(): void {
        this.loginService.showLogin = false;
    }
}
