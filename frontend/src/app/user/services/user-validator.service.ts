import { Injectable } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, ValidatorFn } from '@angular/forms';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ValidationError } from 'src/app/models/validation-error';
import { UserService } from 'src/app/user/services/user.service';

@Injectable({
  providedIn: 'root'
})
export class UserValidatorService {

  constructor(
    public userService: UserService
  ) { }

  passwordConfirmed(): ValidatorFn{
    return (control: AbstractControl): ValidationError => {
      const passwordConfirmed: boolean = control.parent ?
      control.value === control.parent.get('password').value : true;
      return passwordConfirmed ? null : {passwordError: true};
    };
  }

  newPasswordConfirmed(): ValidatorFn {
    return (control: AbstractControl): ValidationError => {
      if (!control.get('oldPassword').value && (control.get('newPassword').value || control.get('newPasswordConfirmation').value)){
        return {oldPasswordError: true};
      }
      if (control.get('newPassword').value !== control.get('newPasswordConfirmation').value){
        return {newPasswordError: true};
      }
      return null;
    };
  }

  hasEmail(id: number): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationError> => {
      return this.userService.hasEmail({id, name: control.value}).pipe(
        map((response: boolean) => !response ? null : {emailError: true})
      );
    };
  }

}
