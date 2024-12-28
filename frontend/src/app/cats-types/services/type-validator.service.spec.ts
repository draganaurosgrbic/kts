import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AbstractControl, ValidationErrors } from '@angular/forms';
import { TypeService } from 'src/app/cats-types/services/type.service';
import { ValidationError } from 'src/app/models/validation-error';
import { Observable, of } from 'rxjs';
import { TypeValidatorService } from './type-validator.service';

describe('TypeValidatorService', () => {
  let service: TypeValidatorService;

  beforeEach(() => {
    const typeServiceMock = {};
    TestBed.configureTestingModule({
      providers: [
        {provide: TypeService, useValue: typeServiceMock},
      ]
    });
    service = TestBed.inject(TypeValidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should recognize taken name', fakeAsync(() => {
    let response: ValidationError;
    service.typeService.hasName = jasmine.createSpy('hasName').and.returnValue(of(true));
    (service.hasName(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toBeNull();
  }));

  it('should recognize free name', fakeAsync(() => {
    let response: ValidationError;
    service.typeService.hasName = jasmine.createSpy('hasName').and.returnValue(of(false));
    (service.hasName(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toEqual({nameError: true});
  }));
});
