import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ClockService {
  private lastSecond = 0;

  // Emits the date in millis every 10 millis
  private timerObservable: Observable<number> = Observable.create(observer => {
    const timerInterval = setInterval(() => {
      observer.next(Date.now());
    }, 10);
  });

  // Emits the date in millis every second, rounded to a whole second
  private secondObservable: Observable<number> = Observable.create(observer => {
    this.timerObservable.subscribe((dateAsMillis: number) => {
      const millis = dateAsMillis % 1000;

      // Use fuzzy logic to allow for small system clock variations
      if (millis > 0 && millis <= 15) {
        if (dateAsMillis - this.lastSecond > 980) {
          // Trim off excess millis
          this.lastSecond = dateAsMillis - millis;
          observer.next(this.lastSecond);
        }
      }
    });
  });

  // Emits the date in millis every minute, rounded to a whole minute
  private minuteObservable: Observable<number> = Observable.create(observer => {
    this.secondObservable.subscribe((dateAsMillis: number) => {
      const seconds = dateAsMillis % 60000;

      // secondObservable emits rounded values, so we can be precise
      if (seconds === 0) {
        observer.next(dateAsMillis);
      }
    });
  });

  // Called every 10 millis
  // Usable for UI animation
  subscribeToTimer(callback: (dateAsMillis: number) => void): void {
    this.timerObservable.subscribe(callback);
  }

  // Called every second
  // Usable for a clock displaying seconds
  subscribeToSeconds(callback: (dateAsMillis: number) => void): void {
    this.secondObservable.subscribe(callback);
  }

  // Called every minuteObservable
  // Usable for a clock displaying minutes
  subscribeToMinutes(callback: (dateAsMillis: number) => void): void {
    this.minuteObservable.subscribe(callback);
  }
}
