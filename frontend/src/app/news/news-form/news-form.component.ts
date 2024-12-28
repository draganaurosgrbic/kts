import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { Image } from 'src/app/models/image';
import { News } from 'src/app/models/news';
import { NewsService } from 'src/app/news/services/news.service';

@Component({
  selector: 'app-news-form',
  templateUrl: './news-form.component.html',
  styleUrls: ['./news-form.component.sass']
})
export class NewsFormComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public news: News,
    public newsService: NewsService,
    public dialogRef: MatDialogRef<NewsFormComponent>,
    public snackBar: MatSnackBar
  ) { }

  savePending = false;
  text: FormControl = new FormControl(this.news.text || '',
  [Validators.required, Validators.pattern(new RegExp('\\S'))]);

  images: Image[] = this.news.images.map(img => {
    return {path: img, upload: null};
  });

  save(): void{
    if (this.text.invalid){
      return;
    }

    const news: News = {
      id: this.news.id,
      culturalOfferId: this.news.culturalOfferId,
      text: this.text.value
    } as News;

    this.savePending = true;
    this.newsService.save(news, this.images).subscribe(
      (result: News) => {
        this.savePending = false;
        if (result){
          this.snackBar.open('News successfully published!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.dialogRef.close();
          this.newsService.announceRefreshData(this.news.culturalOfferId);
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
