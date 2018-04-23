import { NgModule } from '@angular/core';

import { RestCommService } from './rest-comm.service';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [HttpClientModule],
  providers: [RestCommService]
})
export class RestCommModule {}
