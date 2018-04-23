import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ClockService } from './clock.service';

@Component({
  selector: 'app-clock',
  templateUrl: './clock.component.html'
})
export class ClockComponent implements OnInit {
  dateFormatted: String = 'waiting to be set';
  private readonly MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  constructor(private clockService: ClockService) {}

  ngOnInit() {
    this.updateClock(Date.now());
    this.clockService.subscribeToSeconds(this.updateClock.bind(this));
  }

  private updateClock(dateAsMillis: number) {
    const nextDate: Date = new Date(dateAsMillis);
    const month = this.MONTHS[nextDate.getMonth()];
    const day = '' + nextDate.getDate();
    const year = '' + nextDate.getFullYear();
    const hour = '' + nextDate.getHours();
    const minute = ('0' + nextDate.getMinutes()).slice(-2);
    const dateFormatted = month.concat(' ', day, ', ', year, ' ', hour, ':', minute);

    this.dateFormatted = dateFormatted;
  }
}
