import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AbstractControl, ValidationErrors } from '@angular/forms';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { ValidationError } from 'src/app/models/validation-error';
import { CategoryValidatorService } from './category-validator.service';
import { Observable, of } from 'rxjs';


describe('CategoryValidatorService', () => {
  let service: CategoryValidatorService;

  beforeEach(() => {
    const categoryServiceMock = {};
    TestBed.configureTestingModule({
      providers: [
        {provide: CategoryService, useValue: categoryServiceMock}
      ]
    });
    service = TestBed.inject(CategoryValidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should recognize taken name', fakeAsync(() => {
    let response: ValidationError;
    service.categoryService.hasName = jasmine.createSpy('hasName').and.returnValue(of(true));
    (service.hasName(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toBeNull();
  }));

  it('should recognize free name', fakeAsync(() => {
    let response: ValidationError;
    service.categoryService.hasName = jasmine.createSpy('hasName').and.returnValue(of(false));
    (service.hasName(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toEqual({nameError: true});
  }));
});
