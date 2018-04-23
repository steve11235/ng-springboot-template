import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { LoginService } from '../Login/login.service';
import { AppConfigService } from '../App/app-config.service';
import { MessageManagerService as MessageManager } from '../MessageManager/message-manager.service';
import { MessageSeverity } from '../MessageManager/message-severity';

const BASE_HTTP_HEADERS: HttpHeaders = new HttpHeaders();

@Injectable()
export class RestCommService {
  public static MESSAGE_SEVERITY_ERROR = 'Message severity of Error or higher.';

  private processMessagesBound = this.processMessages.bind(this);

  constructor(
    private httpClient: HttpClient,
    private appConfig: AppConfigService,
    private loginService: LoginService,
    private messageManager: MessageManager
  ) {}

  /**
   * Return a cold Observable<any> that returns a DTO on success. Note that messages will be stripped out from the returned DTO and
   * processed before reaching the subscriber. A MESSAGE_SEVERITY_ERROR is thrown if the messages contain a severity of ERROR or SYSTEM.
   *
   * @param resourcePath required, path to the resource, relative to the service path
   * @param queryParms optional, pass a simple object whose fields will be added to the URL as query parameters (do NOT encode values)
   */
  public list(resourcePath: string, queryParams?: any): Observable<any> {
    const fullUrl: string = this.appConfig.hostUrl.concat(this.appConfig.servicePath, resourcePath);
    const options: any = this.buildRequestOptions(this.appConfig.hostUrl, this.loginService.jwt, queryParams);

    const observable: Observable<any> = this.httpClient.get(fullUrl, options).map((dto: any) => this.processMessagesBound(dto));

    return observable;
  }

  /**
   * Return an options object containing HttpHeaders and, optionally, HttpParams.
   *
   * @param hostUrl required
   * @param jwt optional
   * @param queryParams optional
   */
  public buildRequestOptions(hostUrl: string, jwt: string, queryParams: any): any {
    const options: any = {};

    // Remove the method from the host URL
    const hostName: string = hostUrl.slice(hostUrl.indexOf('://') + 3);

    let headers: HttpHeaders = new HttpHeaders()
      .set('Accept', 'application/json')
    if (jwt) {
      headers = headers.set('Authorization', 'bearer'.concat(' ', jwt));
    }
    options.headers = headers;

    if (queryParams) {
      // As of April 2018, passing queryParams to constructor did not work
      let httpParams: HttpParams = new HttpParams();
      for (const key of Object.keys(queryParams)) {
        httpParams = httpParams.set(key, queryParams[key]);
      }
      options.params = httpParams;
    }

    return options;
  }

  /**
   * Process messages from the DTO. Remove the messages property, return the DTO. Throw an error if the message severity is ERROR or SYSTEM.
   *
   * @param dto required the DTO generated from the JSON returned from the service
   */
  public processMessages(dto: any): any {
    const messages: any = dto.messages;
    const noMessagesDto: any = dto;
    delete noMessagesDto.messages;

    if (!messages) {
      throw new Error('Response DTO did not contain messages.');
    }

    this.messageManager.addMessages(messages);

    if (MessageSeverity.isErrorSeverity(messages.maxSeverity)) {
      throw new Error(RestCommService.MESSAGE_SEVERITY_ERROR);
    }

    return noMessagesDto;
  }
}
