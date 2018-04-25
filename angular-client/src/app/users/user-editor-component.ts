import { Component, OnInit } from "@angular/core";
import { User } from "./user";
import { MessageManagerService } from "../../modules/MessageManager/message-manager.service";

@Component({
    selector: 'app-user-editor',
    templateUrl: './user-editor.component.html'
})
export class UserEditorComponent implements OnInit {
    user: User;

    constructor(private messageManager: MessageManagerService) {}

    ngOnInit() {
        this.user = new User(this.messageManager);
    }
}
