import { Entity } from "../../modules/Entity/entity";
import { MessageManagerService } from "../../modules/MessageManager/message-manager.service";
import { MessageSeverity } from "../../modules/MessageManager/message-severity";

export class User implements Entity {
    userKey = 0;
    deactivated = false;
    login = '';
    fullname = '';
    description = '';
    admin = false;

    constructor(private messageManager: MessageManagerService) {}

    validate(): boolean {
        let valid = true;

        if (!Number.isInteger(this.userKey) || this.userKey < 0) {
            valid = false;
            this.messageManager.addMessage("User key is invalid. This an application error.", MessageSeverity.SYSTEM, "User", -1, "userKey");
        }

        if (typeof this.deactivated !== "boolean") {
            valid = false;
            this.messageManager.addMessage("Deactivated is invalid. This an application error.", MessageSeverity.SYSTEM, "User", this.userKey, "deactivated");
        }

        if (typeof this.login !== "string") {
            valid = false;
            this.messageManager.addMessage("Login is invalid. This an application error.", MessageSeverity.SYSTEM, "User", this.userKey, "login");
        }

        if (!this.login) {
            valid = false;
            this.messageManager.addMessage("Login is required.", MessageSeverity.ERROR, "User", this.userKey, "login");
        }

        if (typeof this.fullname !== "string") {
            valid = false;
            this.messageManager.addMessage("Full name is invalid. This an application error.", MessageSeverity.SYSTEM, "User", this.userKey, "fullName");
        }

        if (!this.fullname) {
            valid = false;
            this.messageManager.addMessage("Full name is required.", MessageSeverity.ERROR, "User", this.userKey, "fullName");
        }

        if (typeof this.description !== "string") {
            valid = false;
            this.messageManager.addMessage("Description is invalid. This an application error.", MessageSeverity.SYSTEM, "User", this.userKey, "description");
        }

        if (!this.description) {
            valid = false;
            this.messageManager.addMessage("Description is required.", MessageSeverity.ERROR, "User", this.userKey, "description");
        }

        if (typeof this.admin !== "boolean") {
            valid = false;
            this.messageManager.addMessage("Admin is invalid. This an application error.", MessageSeverity.SYSTEM, "User", this.userKey, "admin");
        }

        return false;
    }
}