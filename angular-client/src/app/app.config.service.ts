import { AppConfig } from '../modules/App/app.config'
import { Injectable } from '@angular/core';

@Injectable()
export class AppConfigService extends AppConfig {

    constructor() {
        super("Angular Client Template", "localhost:8080/rs/");
    }
}
