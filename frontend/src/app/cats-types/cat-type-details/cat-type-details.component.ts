import { Component, OnInit, Input } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DeleteConfirmationComponent } from 'src/app/shared/controls/delete-confirmation/delete-confirmation.component';
import { Category } from 'src/app/models/category';
import { Type } from 'src/app/models/type';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { TypeService } from 'src/app/cats-types/services/type.service';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';

@Component({
  selector: 'app-cat-type-details',
  templateUrl: './cat-type-details.component.html',
  styleUrls: ['./cat-type-details.component.sass']
})
export class CatTypeDetailsComponent implements OnInit {

  constructor(
    public categoryService: CategoryService,
    public typeService: TypeService,
    public dialog: MatDialog
  ) { }

  @Input() catType: Category | Type;

  delete(): void {
    const service = (this.catType as Type).category ? this.typeService : this.categoryService;
    const options: MatDialogConfig = {...DIALOG_OPTIONS, ...{data: () => service.delete(this.catType.id)}};
    this.dialog.open(DeleteConfirmationComponent, options).afterClosed().subscribe(result => {
      if (result){
        service.announceRefreshData();
      }
    });
  }

  ngOnInit(): void {
  }

}
