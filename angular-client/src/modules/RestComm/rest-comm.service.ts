import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';

import { LoginService } from "../Login/login.service";
import { AppConfig } from "../App/app.config";

@Injectable()
export class RestCommService {
  constructor(private httpClient: HttpClient, private loginService: LoginService, private appConfig: AppConfig) {}

  /**
   * Get an array of DTO from the host.
   *
   * @param url required, relative URL to the resource desired
   * @param success required, callback if the request was successful (dto may be an empty array)
   * @param fail required callback if the request failed (error will be a standard string)
   * @param queryParms optional, pass a simple object whose fields will be added to the URL as query parameters (do NOT encode values)
   */
  public list(url: string, success: (dto: any[]) => void, fail: (error: string) => void, queryParams?: any): void {
    const fullUrl: string = this.assembleFullUrl(url, queryParams);
  }

  public assembleFullUrl(url: string, queryParams: any): string {
    let queryString: string = "";
    if (queryParams) {
      queryString = Object.keys(queryParams)
        .map(key => key.concat("=", encodeURIComponent(queryParams[key])))
        .join("&");
    }

    const fullUrl: string = this.appConfig.serviceUrl.concat(url, queryString);

    return fullUrl;
  }
}
