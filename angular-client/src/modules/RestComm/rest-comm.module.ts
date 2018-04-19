import { NgModule } from "@angular/core";
import { HttpClient } from "selenium-webdriver/http";

import { RestCommService } from "./rest-comm.service";

@NgModule({
    imports: [
        HttpClient
    ],
    providers: [
        RestCommService
    ]
})
export class RestCommModule {};