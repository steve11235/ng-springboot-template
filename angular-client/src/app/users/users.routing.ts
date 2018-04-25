import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, Route } from '@angular/router';

import { UsersComponent } from './users.component';
import { UserEditorComponent } from './user-editor-component';

const usersRoutes: Routes  = [
    {path: '', component: UsersComponent, children: [
        {path: 'add', component: UserEditorComponent},
        {path: 'edit/userKey/:userKey', component: UserEditorComponent}
    ]}
];

export const USERS_ROUTES: ModuleWithProviders = RouterModule.forChild(usersRoutes);
