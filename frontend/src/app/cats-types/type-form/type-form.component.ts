import { Component, OnInit, } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, of, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { AUTOCOMPLETE_DEBOUNCE, AUTOCOMPLETE_LENGTH } from 'src/app/constants/autocomplete';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { Image } from 'src/app/models/image';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { TypeService } from 'src/app/cats-types/services/type.service';
import { CategoryValidatorService } from 'src/app/cats-types/services/category-validator.service';
import { TypeValidatorService } from 'src/app/cats-types/services/type-validator.service';
import { Type } from 'src/app/models/type';

@Component({
  selector: 'app-type-form',
  templateUrl: './type-form.component.html',
  styleUrls: ['./type-form.component.sass']
})
export class TypeFormComponent implements OnInit {

  constructor(
    public typeService: TypeService,
    public categoryService: CategoryService,
    public typeValidator: TypeValidatorService,
    public categoryValidator: CategoryValidatorService,
    public snackBar: MatSnackBar
  ) { }

  savePending = false;
  image: Image = {path: null, upload: null};
  typeForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required,
      Validators.pattern(new RegExp('\\S'))],
      [this.typeValidator.hasName(true)]),
    category: new FormControl('', [Validators.required,
      Validators.pattern(new RegExp('\\S'))],
      [this.categoryValidator.hasName(false)])
  });

  categoryFilters: Subject<string> = new Subject<string>();
  categories: Observable<string[]> = this.categoryFilters.pipe(
    debounceTime(AUTOCOMPLETE_DEBOUNCE),
    distinctUntilChanged(),
    switchMap((filter: string) => filter.length >= AUTOCOMPLETE_LENGTH ?
    this.categoryService.filterNames(filter) : of([]))
  );

  save(): void{
    if (this.typeForm.invalid){
      return;
    }

    this.savePending = true;
    this.typeService.save(this.typeForm.value, this.image).subscribe(
      (response: Type) => {
        this.savePending = false;
        if (response){
          this.snackBar.open('Type successfully added!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.typeForm.reset();
          this.typeService.announceRefreshData();
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
