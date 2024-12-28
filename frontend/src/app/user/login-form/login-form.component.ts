import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE } from 'src/app/constants/snackbar';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/user/services/user.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.sass']
})
export class LoginFormComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public userService: UserService,
    public router: Router,
    public snackBar: MatSnackBar
  ) { }

  loginPending = false;
  loginForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required,
    Validators.pattern(new RegExp('\\S'))]),
    password: new FormControl('', [Validators.required,
    Validators.pattern(new RegExp('\\S'))])
  });

  login(): void{
    if (this.loginForm.invalid){
      return;
    }
    this.loginPending = true;
    this.userService.login(this.loginForm.value).subscribe(
      (user: User) => {
        this.loginPending = false;
        if (user){
          this.authService.saveUser(user);
          this.router.navigate(['/']);
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
