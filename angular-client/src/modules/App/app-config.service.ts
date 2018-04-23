import { Injectable } from "@angular/core";

@Injectable()
export class AppConfigService {
  /**
   * Constructor
   *
   * @param title required, application title
   * @param hostUrl required, service host method, name, and port, e.g., http://localhost:8080; port can be omitted if default
   * @param servicePath required, path to the service; e.g., /rs/
   * @param additionalValues option, a simple object containing any additional configuration values
   */
  constructor(public title: string, public hostUrl: string, public servicePath: string, public additionalValues: any = null) {}
}
