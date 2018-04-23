import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { UsersComponent } from './users.component';
import { USERS_ROUTES } from './uses.routing';

@NgModule({
  declarations: [UsersComponent],
  imports: [
    CommonModule,
    FormsModule,
    USERS_ROUTES // must be last
  ],
  exports: [UsersComponent]
})
export class UsersModule {}
