import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { CatTypeDetailsComponent } from './cat-type-details/cat-type-details.component';
import { CatTypeDialogComponent } from './cat-type-dialog/cat-type-dialog.component';
import { CatFormComponent } from './cat-form/cat-form.component';
import { CatTypeListComponent } from './cat-type-list/cat-type-list.component';
import { TypeFormComponent } from './type-form/type-form.component';

@NgModule({
  declarations: [
    CatTypeDetailsComponent,
    CatTypeListComponent,
    CatFormComponent,
    TypeFormComponent,
    CatTypeDialogComponent
  ],
  imports: [
    SharedModule
  ],
  exports: [
    CatTypeDialogComponent
  ]
})
export class CatsTypesModule { }
