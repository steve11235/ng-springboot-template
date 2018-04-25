import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';

import { MessageManagerService } from '../../modules/MessageManager/message-manager.service';
import { RestCommService } from '../../modules/RestComm/rest-comm.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  userList = [];
  private processListDtoBound: (dto: any) => void = this.processListDto.bind(this);

  constructor(private messageManager: MessageManagerService, private restComm: RestCommService) {}

  ngOnInit() {
    this.restComm.list('users').subscribe(this.processListDtoBound, this.handleListError);
  }

  private processListDto(dto: any): void {
    this.userList = dto.userList;
  }

  private handleListError(err: any): void {
    let message = '';
    for (const key of Object.keys(err)) {
      message = message.concat('\n', key, ': ', err[key]);
    }
    alert('Oops! ' + message);
  }

  handleRowSelected(user: any): void {
    alert('Click: ' + user.userKey);
  }
}
