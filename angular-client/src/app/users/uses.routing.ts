import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, Route } from '@angular/router';

import { UsersComponent } from './users.component';

const usersRoutes: Routes  = [
    {path: 'users', component: UsersComponent}
];

export const USERS_ROUTES: ModuleWithProviders = RouterModule.forChild(usersRoutes);
