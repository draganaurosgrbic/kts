import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { GUEST_ROLE } from 'src/app/constants/roles';
import { LOGIN_PATH, USER_PATH } from 'src/app/constants/router';
import { AuthService } from 'src/app/shared/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class GuestGuard implements CanActivate {

  constructor(
    public authService: AuthService,
    public router: Router
  ){}

  canActivate(): boolean {
    if (this.authService.getUser()?.role !== GUEST_ROLE){
      this.router.navigate([`${USER_PATH}/${LOGIN_PATH}`]);
      return false;
    }
    return true;
  }

}
