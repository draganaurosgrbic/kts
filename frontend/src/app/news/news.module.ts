import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { NewsListComponent } from './news-list/news-list.component';
import { NewsDetailsComponent } from './news-details/news-details.component';
import { NewsFormComponent } from './news-form/news-form.component';

@NgModule({
  declarations: [
    NewsDetailsComponent,
    NewsListComponent,
    NewsFormComponent
  ],
  imports: [
    SharedModule
  ],
  exports: [
    NewsListComponent
  ]
})
export class NewsModule { }
