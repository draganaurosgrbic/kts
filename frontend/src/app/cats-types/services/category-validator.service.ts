import { Injectable } from '@angular/core';
import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ValidationError } from 'src/app/models/validation-error';
import { CategoryService } from 'src/app/cats-types/services/category.service';

@Injectable({
  providedIn: 'root'
})
export class CategoryValidatorService {

  constructor(
    public categoryService: CategoryService
  ) { }

  hasName(toBeUnique: boolean): AsyncValidatorFn{
    return (control: AbstractControl): Observable<null | ValidationError> => {
      return this.categoryService.hasName({id: null, name: control.value}).pipe(
        map((response: boolean) => {
          if (toBeUnique){
            return !response ? null : {nameError: true};
          }
          return response ? null : {nameError: true};
        })
      );
    };
  }

}
