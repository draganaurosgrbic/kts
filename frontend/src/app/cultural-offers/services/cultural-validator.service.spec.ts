import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AbstractControl, ValidationErrors } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { ValidationError } from 'src/app/models/validation-error';
import { CulturalValidatorService } from './cultural-validator.service';

describe('CulturalValidatorService', () => {
  let service: CulturalValidatorService;

  beforeEach(() => {
    const culturalServiceMock = {};
    TestBed.configureTestingModule({
      providers: [
        {provide: CulturalService, useValue: culturalServiceMock}
      ]
    });
    service = TestBed.inject(CulturalValidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should recognize null lat and lng', () => {
    expect(service.locationFound({lat: null, lng: null})(null)).toEqual({locationError: true});
  });

  it('should recognize null lat', () => {
    expect(service.locationFound({lat: null, lng: 10})(null)).toEqual({locationError: true});
  });

  it('should recognize null lng', () => {
    expect(service.locationFound({lat: 10, lng: null})(null)).toEqual({locationError: true});
  });

  it('should recognize valid location', () => {
    expect(service.locationFound({lat: 10, lng: 10})(null)).toBeNull();
  });

  it('should recognize taken name', fakeAsync(() => {
    let response: ValidationError;
    service.culturalService.hasName = jasmine.createSpy('hasName').and.returnValue(of(true));
    (service.hasName(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toEqual({nameError: true});
  }));

  it('should recognize free name', fakeAsync(() => {
    let response: ValidationError;
    service.culturalService.hasName = jasmine.createSpy('hasName').and.returnValue(of(false));
    (service.hasName(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toBeNull();
  }));

});
