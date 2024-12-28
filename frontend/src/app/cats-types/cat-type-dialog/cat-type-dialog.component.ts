import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-cat-type-dialog',
  templateUrl: './cat-type-dialog.component.html',
  styleUrls: ['./cat-type-dialog.component.sass']
})
export class CatTypeDialogComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public cats: boolean,
    public dialogRef: MatDialogRef<CatTypeDialogComponent>
  ) { }

  ngOnInit(): void {
  }

}
