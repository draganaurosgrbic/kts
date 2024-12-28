import { Injectable } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, ValidatorFn } from '@angular/forms';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Geolocation } from 'src/app/models/geolocation';
import { ValidationError } from 'src/app/models/validation-error';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';

@Injectable({
  providedIn: 'root'
})
export class CulturalValidatorService {

  constructor(
    public culturalService: CulturalService
  ) { }

  locationFound(geolocation: Geolocation): ValidatorFn{
    return (): ValidationError => {
      const empty = [null, undefined];
      return (empty.includes(geolocation.lat) || empty.includes(geolocation.lng)) ? {locationError: true} : null;
    };
  }

  hasName(id: number): AsyncValidatorFn{
    return (control: AbstractControl): Observable<ValidationError> => {
      return this.culturalService.hasName({id, name: control.value}).pipe(
        map((response: boolean) => !response ? null : {nameError: true}));
    };
  }

}
