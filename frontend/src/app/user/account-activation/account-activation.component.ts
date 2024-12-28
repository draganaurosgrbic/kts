import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { LOGIN_PATH, USER_PATH } from 'src/app/constants/router';
import { UserService } from 'src/app/user/services/user.service';

@Component({
  selector: 'app-account-activation',
  templateUrl: './account-activation.component.html',
  styleUrls: ['./account-activation.component.sass']
})
export class AccountActivationComponent implements OnInit {

  constructor(
    public userService: UserService,
    public router: Router,
    public route: ActivatedRoute,
    public snackBar: MatSnackBar
  ) { }

  activatePending = true;

  ngOnInit(): void {
    const code: string = this.route.snapshot.params.code;
    this.userService.activate(code).subscribe(
      (response: boolean) => {
        this.activatePending = false;
        if (response){
          this.router.navigate([`${USER_PATH}/${LOGIN_PATH}`]);
          this.snackBar.open('Your account has been activated! You can login now.',
          SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
        }
      }
    );
  }

}
