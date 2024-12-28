import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE } from 'src/app/constants/snackbar';
import { Comment } from 'src/app/models/comment';
import { Image } from 'src/app/models/image';
import { CommentService } from 'src/app/comments/services/comment.service';

@Component({
  selector: 'app-comment-form',
  templateUrl: './comment-form.component.html',
  styleUrls: ['./comment-form.component.sass']
})
export class CommentFormComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public comment: Comment,
    public commentService: CommentService,
    public dialogRef: MatDialogRef<CommentFormComponent>,
    public snackBar: MatSnackBar
  ) { }

  savePending = false;
  rate: number = this.comment.rate || 0;
  text: FormControl = new FormControl(this.comment.text || '',
  [Validators.required, Validators.pattern(new RegExp('\\S'))]);

  images: Image[] = this.comment.images.map(img => {
    return {path: img, upload: null};
  });

  save(): void{
    if (this.text.invalid){
      return;
    }

    const comment: Comment = {
      id: this.comment.id,
      culturalOfferId: this.comment.culturalOfferId,
      rate: this.rate,
      text: this.text.value
    } as Comment;

    this.savePending = true;
    this.commentService.save(comment, this.images).subscribe(
      (result: number) => {
        this.savePending = false;
        if (result !== undefined && result !== null){
          this.dialogRef.close();
          this.commentService.announceRefreshData({id: this.comment.culturalOfferId, totalRate: result});
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
