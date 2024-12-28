import { NgModule } from '@angular/core';
import { UserRoutingModule } from './user-routing.module';
import { SharedModule } from '../shared/shared.module';
import { UserComponent } from './user.component';
import { AccountActivationComponent } from './account-activation/account-activation.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { RegisterFormComponent } from './register-form/register-form.component';
import { ProfileFormComponent } from './profile-form/profile-form.component';
import { ProfileDetailsComponent } from './profile-details/profile-details.component';

@NgModule({
  declarations: [
    UserComponent,
    LoginFormComponent,
    RegisterFormComponent,
    ProfileFormComponent,
    AccountActivationComponent,
    ProfileDetailsComponent
  ],
  imports: [
    UserRoutingModule,
    SharedModule
  ]
})
export class UserModule { }
