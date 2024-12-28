import { Injectable } from '@angular/core';
import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ValidationError } from 'src/app/models/validation-error';
import { TypeService } from 'src/app/cats-types/services/type.service';

@Injectable({
  providedIn: 'root'
})
export class TypeValidatorService {

  constructor(
    public typeService: TypeService
  ) { }

  hasName(toBeUnique: boolean): AsyncValidatorFn{
    return (control: AbstractControl): Observable<null | ValidationError> => {
      return this.typeService.hasName({id: null, name: control.value}).pipe(
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
