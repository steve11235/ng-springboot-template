import { AppConfig } from '../App/app.config';
import { RestCommService } from './rest-comm.service';
import { HttpHeaders, HttpParams, HttpClient, HttpHandler, HttpXhrBackend, XhrFactory } from '@angular/common/http';

describe('buildRequestOptions tests', () => {
  it('should produce a full options', () => {
    const queryParams: any = {
      key1: 'value!@#',
      key2: 'value$%^'
    };

    const options: any = new RestCommService(null, null, null, null).buildRequestOptions(
      'http://localhost:8080',
      'foo.bar.baz',
      queryParams
    );

    const headers: HttpHeaders = options.headers;
    const headersString: string = headers
      .keys()
      .map(key => key.concat(': ', headers.get(key)))
      .join(', ');

    expect(headersString).toEqual(
      'Accept: application/json, Accept-Charset: utf-8, Host: localhost:8080, Authorization: bearer foo.bar.baz'
    );

    const params: HttpParams = options.params;
    const paramsString: string = params
      .keys()
      .map(key => key.concat(': ', params.get(key)))
      .join(', ');

    expect(paramsString).toEqual('key1: value!@#, key2: value$%^');
  });

  it('should produce an options with no authorization, query params', () => {
    const queryParams: any = {
      key1: 'value!@#',
      key2: 'value$%^'
    };

    const options: any = new RestCommService(null, null, null, null).buildRequestOptions('http://localhost:8080', null, null);

    const headers: HttpHeaders = options.headers;
    const headersString: string = headers
      .keys()
      .map(key => key.concat(': ', headers.get(key)))
      .join(', ');

    expect(headersString).toEqual('Accept: application/json, Accept-Charset: utf-8, Host: localhost:8080');

    const params: HttpParams = options.params;
    expect(params).toEqual(undefined);
  });
});
