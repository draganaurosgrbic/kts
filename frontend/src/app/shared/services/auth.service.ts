import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  readonly STORAGE_KEY = 'auth';

  saveUser(profile: User): void{
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(profile));
  }

  deleteUser(): void{
    localStorage.removeItem(this.STORAGE_KEY);
  }

  getUser(): User{
    return JSON.parse(localStorage.getItem(this.STORAGE_KEY));
  }

}
