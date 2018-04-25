import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { UsersComponent } from './users.component';
import { USERS_ROUTES } from './users.routing';
import { UserEditorComponent } from './user-editor-component';

@NgModule({
  declarations: [UsersComponent, UserEditorComponent],
  imports: [
    CommonModule,
    FormsModule,
    USERS_ROUTES // must be last
  ],
  exports: [UsersComponent]
})
export class UsersModule {}
