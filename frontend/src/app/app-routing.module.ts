import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HOME_PATH, USER_PATH } from './constants/router';
import { HomeComponent } from './main/home/home.component';

const routes: Routes = [
  {
    path: HOME_PATH,
    component: HomeComponent
  },
  {
    path: USER_PATH,
    loadChildren: () => import('./user/user.module').then(m => m.UserModule)
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: HOME_PATH
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
