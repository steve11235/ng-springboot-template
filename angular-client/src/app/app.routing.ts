import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule, Route, PreloadAllModules } from '@angular/router';

import { HomeComponent } from './home/home.component';

const appRoutes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'users', loadChildren: 'app/users/users.module#UsersModule' }
];

export const APP_ROUTES: ModuleWithProviders = RouterModule.forRoot(appRoutes, { preloadingStrategy: PreloadAllModules });
