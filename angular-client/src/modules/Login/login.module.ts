import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login.component';
import { LoginService } from './login.service';

@NgModule({
  declarations: [LoginComponent],
  imports: [CommonModule],
  exports: [LoginComponent],
  providers: [LoginService],
  bootstrap: [LoginComponent]
})
export class LoginModule {}
