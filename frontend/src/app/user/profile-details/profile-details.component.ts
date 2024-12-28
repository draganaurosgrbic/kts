import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';
import { AuthService } from 'src/app/shared/services/auth.service';
import { User } from 'src/app/models/user';
import { PROFILE_PATH, USER_PATH } from 'src/app/constants/router';

@Component({
  selector: 'app-details-profile',
  templateUrl: './profile-details.component.html',
  styleUrls: ['./profile-details.component.sass']
})
export class ProfileDetailsComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public router: Router,
    public dialogRef: MatDialogRef<ProfileDetailsComponent>
  ) { }

  profile: User = this.authService.getUser();

  edit(): void{
    this.dialogRef.close();
    this.router.navigate([`${USER_PATH}/${PROFILE_PATH}`]);
  }

  ngOnInit(): void {
  }

}
