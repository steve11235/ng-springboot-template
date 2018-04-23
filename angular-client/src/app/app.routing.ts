import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, Route } from '@angular/router';

import { HomeComponent } from './home/home.component';

const appRoutes: Routes  = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'home', component: HomeComponent}
];

export const APP_ROUTES: ModuleWithProviders = RouterModule.forRoot(appRoutes);
