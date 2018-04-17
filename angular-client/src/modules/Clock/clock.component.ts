import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { ClockService } from "./clock.service";

@Component({
  selector: "clock",
  templateUrl: "./clock.component.html"
})
export class ClockComponent implements OnInit {
  dateFormatted: String = "waiting to be set";

  constructor(private clockService: ClockService) {}

  ngOnInit() {
    this.updateClock(Date.now());
    this.clockService.subscribeToSeconds(this.updateClock.bind(this));
  }

  private updateClock(dateAsMillis: number) {
    const nextDate: Date = new Date(dateAsMillis);
    const month = String(nextDate.getMonth() + 1);
    const day = String(nextDate.getDate());
    const year = String(nextDate.getFullYear());
    const hour = String(nextDate.getHours());
    const minute = ("0" + String(nextDate.getMinutes())).slice(-2);
    const dateFormatted = month + "/" + day + "/" + year + " " + hour + ":" + minute;

    this.dateFormatted = dateFormatted;
  }
}
