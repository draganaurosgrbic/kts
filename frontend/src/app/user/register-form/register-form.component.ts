import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { UserService } from 'src/app/user/services/user.service';
import { UserValidatorService } from 'src/app/user/services/user-validator.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.sass']
})
export class RegisterFormComponent implements OnInit {

  constructor(
    public userService: UserService,
    public formValidator: UserValidatorService,
    public snackBar: MatSnackBar
    ) { }

  registerPending = false;
  registerForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required,
      Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$')],
      [this.formValidator.hasEmail(null)]),
    firstName: new FormControl('', [Validators.required,
      Validators.pattern(new RegExp('\\S'))]),
    lastName: new FormControl('', [Validators.required,
      Validators.pattern(new RegExp('\\S'))]),
    password: new FormControl('', [Validators.required,
      Validators.pattern(new RegExp('\\S'))]),
    passwordConfirmation: new FormControl('', [this.formValidator.passwordConfirmed()])
  });

  register(): void{
    if (this.registerForm.invalid){
      return;
    }
    this.registerPending = true;
    this.userService.register(this.registerForm.value).subscribe(
      (response: boolean) => {
        this.registerPending = false;
        if (response){
          this.snackBar.open('Your request has been sent! Check your email.',
          SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.registerForm.reset();
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

  ngOnInit(): void {
    this.registerForm.get('password').valueChanges.subscribe(
      () => {
        this.registerForm.get('passwordConfirmation').updateValueAndValidity();
      }
    );
  }

}
