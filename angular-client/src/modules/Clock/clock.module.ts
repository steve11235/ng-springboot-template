import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClockService } from './clock.service';
import { ClockComponent } from './clock.component';

@NgModule({
  declarations: [ClockComponent],
  imports: [CommonModule],
  exports: [ClockComponent],
  providers: [ClockService],
  bootstrap: [ClockComponent]
})
export class ClockModule {}
