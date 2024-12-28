import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { CategoryValidatorService } from 'src/app/cats-types/services/category-validator.service';
import { CatFormComponent } from './cat-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { Category } from 'src/app/models/category';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';

describe('CatFormComponent', () => {
  let component: CatFormComponent;
  let fixture: ComponentFixture<CatFormComponent>;
  const nameValidator: any = {};

  beforeEach(async () => {

    const categoryServiceMock = {
      save: jasmine.createSpy('save'),
      announceRefreshData: jasmine.createSpy('announceRefreshData'),
    };
    const categoryValidatorMock = {
      hasName: jasmine.createSpy('hasName').and.returnValue(() => of(nameValidator)),
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ CatFormComponent ],
      imports: [ReactiveFormsModule, FormsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: CategoryService, useValue: categoryServiceMock},
        {provide: CategoryValidatorService, useValue: categoryValidatorMock},
        {provide: MatSnackBar, useValue: snackBarMock}

      ]
    })
    .compileComponents();
  });

  afterEach(() => {
    delete nameValidator.nameError;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CatFormComponent);
    component = fixture.componentInstance;
    component.savePending = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.savePending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('div'))[1].nativeElement.classList).toContain('disabled');
  });

  it('should recognize empty form', () => {
    component.categoryForm.reset({
      name: ''
    });
    expect(component.categoryForm.valid).toBeFalse();
    expect(component.categoryForm.controls.name.valid).toBeFalse();
    expect(component.categoryForm.controls.name.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name is required!');
  });

  it('should recognize blank form', () => {
    component.categoryForm.reset({
      name: '  '
    });
    expect(component.categoryForm.valid).toBeFalse();
    expect(component.categoryForm.controls.name.valid).toBeFalse();
    expect(component.categoryForm.controls.name.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name is required!');
  });

  it('should recognize taken name', () => {
    nameValidator.nameError = true;
    component.categoryForm.reset({
      name: 'asd'
    });
    expect(component.categoryForm.valid).toBeFalse();
    expect(component.categoryForm.controls.name.valid).toBeFalse();
    expect(component.categoryForm.controls.name.errors.nameError).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name already exists!');
  });

  it('should recognize valid form', () => {
    component.categoryForm.reset({
      name: 'asd'
    });
    expect(component.categoryForm.valid).toBeTrue();
  });

  it('should prevent invalid form submit', () => {
    component.categoryForm.reset({
      name: ''
    });
    component.save();
    expect(component.categoryService.save).not.toHaveBeenCalled();
  });

  it('should notify invalid save', () => {
    (component.categoryService.save as any).and.returnValue(of(null));
    component.categoryForm.reset({
      name: 'asd'
    });
    component.save();
    expect(component.categoryService.save).toHaveBeenCalledTimes(1);
    expect(component.categoryService.save).toHaveBeenCalledWith( {
      name: component.categoryForm.value.name,
    } as Category);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

  it('should notify valid save', () => {
    (component.categoryService.save as any).and.returnValue(of({}));
    component.categoryForm.reset({
      name: 'asd'
    });
    component.save();
    expect(component.categoryService.save).toHaveBeenCalledTimes(1);
    expect(component.categoryService.save).toHaveBeenCalledWith( {
      name: 'asd'
    } as Category);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('Category successfully added!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
  });
});
