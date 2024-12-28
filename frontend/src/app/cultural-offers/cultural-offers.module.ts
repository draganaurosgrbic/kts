import { NgModule } from '@angular/core';
import { CulturalDetailsComponent } from './cultural-details/cultural-details.component';
import { SharedModule } from '../shared/shared.module';
import { CulturalListComponent } from './cultural-list/cultural-list.component';
import { CulturalDialogComponent } from './cultural-dialog/cultural-dialog.component';
import { NewsModule } from '../news/news.module';
import { CommentsModule } from '../comments/comments.module';
import { CulturalFormComponent } from './cultural-form/cultural-form.component';
import { FilterFormComponent } from './cultural-list/filter-form/filter-form.component';

@NgModule({
  declarations: [
    CulturalDetailsComponent,
    CulturalListComponent,
    CulturalDialogComponent,
    CulturalFormComponent,
    FilterFormComponent
  ],
  imports: [
    SharedModule,
    CommentsModule,
    NewsModule
  ],
  exports: [
    CulturalListComponent
  ]
})
export class CulturalOffersModule { }
