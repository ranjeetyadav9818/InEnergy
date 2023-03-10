import {ModuleWithProviders} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {DashBoardComponent} from './components/homepage/dashboard.component';
import {InvoicesComponent} from './components/invoices/invoices.component';
import {ProfileComponent} from './components/profile/profile.component';
import {ProgramsSummaryComponent} from './components/programs/programs.summary.component';

export const routes: Routes = [

  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'homepage',
    component: DashBoardComponent
  },
  {
    path: 'profile',
    component: ProfileComponent
  },
  {
    path: 'invoices',
    component: InvoicesComponent
  },
  {
    path: 'programsSummary',
    component: ProgramsSummaryComponent
  }
];

export const RoutingModule: ModuleWithProviders = RouterModule.forRoot(routes);
