import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { ADMIN_ROLE } from 'src/app/constants/roles';
import { News } from 'src/app/models/news';
import { NewsService } from 'src/app/news/services/news.service';
import { DeleteConfirmationComponent } from 'src/app/shared/controls/delete-confirmation/delete-confirmation.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { NewsFormComponent } from '../news-form/news-form.component';

@Component({
  selector: 'app-news-details',
  templateUrl: './news-details.component.html',
  styleUrls: ['./news-details.component.sass']
})
export class NewsDetailsComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public newsService: NewsService,
    public dialog: MatDialog
  ) { }

  @Input() news: News;

  get admin(): boolean{
    return this.authService.getUser()?.role === ADMIN_ROLE;
  }

  edit(): void {
    const options: MatDialogConfig = {...DIALOG_OPTIONS, ...{data: this.news}};
    this.dialog.open(NewsFormComponent, options);
  }

  delete(): void {
    const options: MatDialogConfig = {...DIALOG_OPTIONS, ...{data: () => this.newsService.delete(this.news.id)}};
    this.dialog.open(DeleteConfirmationComponent, options).afterClosed().subscribe(result => {
      if (result || typeof result === 'number'){
        this.newsService.announceRefreshData(this.news.culturalOfferId);
      }
    });
  }

  ngOnInit(): void {
  }

}
