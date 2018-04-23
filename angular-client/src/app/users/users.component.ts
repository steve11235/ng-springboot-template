import { Component, OnInit } from '@angular/core';
import { MessageManagerService } from '../../modules/MessageManager/message-manager.service';
import { RestCommService } from '../../modules/RestComm/rest-comm.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {
  constructor(private messageManager: MessageManagerService, private restComm: RestCommService) {}

  ngOnInit() {}
}
