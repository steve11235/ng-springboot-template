import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, Route } from '@angular/router';

import { HomeComponent } from './app/home/home.component';
import { UsersComponent } from './app/users/users.component';

const appRoutes: Routes  = [
    {path: "", redirectTo: "home", pathMatch: "full"},
    {path: "home", component: HomeComponent},
    {path: "users", component: UsersComponent}
];

export const APP_ROUTES: ModuleWithProviders = RouterModule.forRoot(appRoutes);