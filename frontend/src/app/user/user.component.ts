import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LOGIN_PATH, PROFILE_PATH, REGISTER_PATH, USER_PATH } from '../constants/router';
import { AuthService } from '../shared/services/auth.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.sass']
})
export class UserComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public router: Router,
    public location: Location
  ) { }

  get login(): boolean{
    return this.router.url.includes(LOGIN_PATH);
  }

  get profile(): boolean{
    return this.router.url.includes(PROFILE_PATH);
  }

  signOut(): void{
    this.authService.deleteUser();
    this.router.navigate([`${USER_PATH}/${LOGIN_PATH}`]);
  }

  toggleAuth(): void{
    const path = this.login ? REGISTER_PATH : LOGIN_PATH;
    this.router.navigate([`${USER_PATH}/${path}`]);
  }

  ngOnInit(): void {
  }

}
