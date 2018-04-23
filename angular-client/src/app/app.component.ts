import { Component, OnInit } from '@angular/core';

import { MessageManagerComponent } from '../modules/MessageManager/message-manager.component';
import { MessageManagerService } from '../modules/MessageManager/message-manager.service';
import { MessageSeverity } from '../modules/MessageManager/message-severity';
import { LoginService } from '../modules/Login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  readonly title = 'Angular Client Template';

  constructor(public loginService: LoginService) {}
}
