import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { Image } from 'src/app/models/image';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/user/services/user.service';
import { UserValidatorService } from 'src/app/user/services/user-validator.service';

@Component({
  selector: 'app-profile-form',
  templateUrl: './profile-form.component.html',
  styleUrls: ['./profile-form.component.sass']
})
export class ProfileFormComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public userService: UserService,
    public userValidator: UserValidatorService,
    public snackBar: MatSnackBar
  ) { }

  savePending = false;
  image: Image = {path: this.authService.getUser().image, upload: null};
  profileForm: FormGroup = new FormGroup({
    email: new FormControl(this.authService.getUser().email,
      [Validators.required,
      Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$')],
      [this.userValidator.hasEmail(this.authService.getUser().id)]),
    firstName: new FormControl(this.authService.getUser().firstName,
      [Validators.required, Validators.pattern(new RegExp('\\S'))]),
    lastName: new FormControl(this.authService.getUser().lastName,
      [Validators.required, Validators.pattern(new RegExp('\\S'))]),
    oldPassword: new FormControl(''),
    newPassword: new FormControl(''),
    newPasswordConfirmation: new FormControl('')
  }, {
    validators: [this.userValidator.newPasswordConfirmed()]
  });

  resetPassword(): void{
    this.profileForm.get('oldPassword').setValue('');
    this.profileForm.get('newPassword').setValue('');
    this.profileForm.get('newPasswordConfirmation').setValue('');
  }

  save(): void{
    if (this.profileForm.invalid){
      return;
    }
    this.savePending = true;
    this.userService.update(this.profileForm.value, this.image).subscribe(
      (user: User) => {
        this.savePending = false;
        this.resetPassword();
        if (user){
          this.authService.saveUser(user);
          this.snackBar.open('Your profile has been updated!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

  ngOnInit(): void {
  }

}
