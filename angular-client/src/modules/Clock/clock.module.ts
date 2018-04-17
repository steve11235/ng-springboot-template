import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { ClockService } from "./clock.service";
import { ClockComponent } from "./clock.component";

@NgModule({
    declarations: [
        ClockComponent
    ],
    imports: [
        BrowserModule
    ],
    exports: [
        ClockComponent
    ],
    providers: [
        ClockService
    ],
    bootstrap: [
        ClockComponent
    ]
})
export class ClockModule {}