import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS, DELETE_SUCCESS, DELETE_ERROR } from 'src/app/constants/snackbar';

@Component({
  selector: 'app-delete-confirmation',
  templateUrl: './delete-confirmation.component.html',
  styleUrls: ['./delete-confirmation.component.sass']
})
export class DeleteConfirmationComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public deleteFunction: () => Observable<boolean | number>,
    public dialogRef: MatDialogRef<DeleteConfirmationComponent>,
    public snackBar: MatSnackBar
  ) { }

  deletePending = false;

  confirm(): void{
    this.deletePending = true;
    this.deleteFunction().subscribe(
      (param: boolean | number) => {
        this.deletePending = false;
        if (param || typeof param === 'number'){
          this.dialogRef.close(param);
          this.snackBar.open(DELETE_SUCCESS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
        }
        else{
          this.snackBar.open(DELETE_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );
  }

  ngOnInit(): void {
  }

}
