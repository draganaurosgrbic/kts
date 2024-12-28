import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { TypeService } from 'src/app/cats-types/services/type.service';
import { TypeValidatorService } from 'src/app/cats-types/services/type-validator.service';
import { TypeFormComponent } from './type-form.component';
import { CategoryValidatorService } from '../services/category-validator.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Type } from 'src/app/models/type';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';

describe('TypeFormComponent', () => {
  let component: TypeFormComponent;
  let fixture: ComponentFixture<TypeFormComponent>;
  const categoryFilters: string[] = ['category1', 'category2', 'category3'];
  const typeValidator: any = {};
  const categoryValidator: any = {};

  beforeEach(async () => {
    const typeServiceMock = {
      save: jasmine.createSpy('save'),
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const categoryServiceMock = {
      filterNames: jasmine.createSpy('filterNames').and.returnValue(of(categoryFilters))
    };
    const typeValidatorMock = {
      hasName: jasmine.createSpy('hasName').and.returnValue(() => of(typeValidator))
    };
    const categoryValidatorMock = {
      hasName: jasmine.createSpy('hasName').and.returnValue(() => of(categoryValidator))
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ TypeFormComponent ],
      imports: [MatAutocompleteModule, ReactiveFormsModule, FormsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: TypeService, useValue: typeServiceMock},
        {provide: CategoryService, useValue: categoryServiceMock},
        {provide: TypeValidatorService, useValue: typeValidatorMock},
        {provide: CategoryValidatorService, useValue: categoryValidatorMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  afterEach(() => {
    delete typeValidator.nameError;
    delete categoryValidator.nameError;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeFormComponent);
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
    component.typeForm.reset({
      name: '',
      category: ''
    });
    expect(component.typeForm.valid).toBeFalse();
    expect(component.typeForm.controls.name.valid).toBeFalse();
    expect(component.typeForm.controls.name.errors.required).toBeTruthy();
    expect(component.typeForm.controls.category.valid).toBeFalse();
    expect(component.typeForm.controls.category.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Category is required!');
  });

  it('should recognize blank form', () => {
    component.typeForm.reset({
      name: '  ',
      category: '  '
    });
    expect(component.typeForm.valid).toBeFalse();
    expect(component.typeForm.controls.name.valid).toBeFalse();
    expect(component.typeForm.controls.name.errors.pattern).toBeTruthy();
    expect(component.typeForm.controls.category.valid).toBeFalse();
    expect(component.typeForm.controls.category.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Category is required!');
  });

  it('should recognize taken name', () => {
    typeValidator.nameError = true;
    component.typeForm.reset({
      name: 'asd',
      category: ''
    });
    expect(component.typeForm.valid).toBeFalse();
    expect(component.typeForm.controls.name.valid).toBeFalse();
    expect(component.typeForm.controls.name.errors.nameError).toBeTruthy();
    expect(component.typeForm.controls.category.valid).toBeFalse();
    expect(component.typeForm.controls.category.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name already exists!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Category is required!');
  });

  it('should recognize non existing category', () => {
    categoryValidator.nameError = true;
    component.typeForm.reset({
      name: '',
      category: 'asd',
    });
    expect(component.typeForm.valid).toBeFalse();
    expect(component.typeForm.controls.category.valid).toBeFalse();
    expect(component.typeForm.controls.name.valid).toBeFalse();
    expect(component.typeForm.controls.category.errors.nameError).toBeTruthy();
    expect(component.typeForm.controls.name.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Category not found!');
  });

  it('should recognize valid form', () => {
    component.typeForm.reset({
      name: 'asd',
      category: 'asd'
    });
    expect(component.typeForm.valid).toBeTrue();
  });

  it('should prevent invalid form submit', () => {
    component.typeForm.reset({
      name: '',
      category: ''
    });
    component.save();
    expect(component.typeService.save).not.toHaveBeenCalled();
  });

  it('should notify invalid save', () => {
    (component.typeService.save as any).and.returnValue(of(null));
    component.typeForm.reset({
      name: 'asd',
      category: 'asd'
    });
    component.save();
    expect(component.typeService.save).toHaveBeenCalledTimes(1);
    expect(component.typeService.save).toHaveBeenCalledWith( {
      name: component.typeForm.value.name,
      category: component.typeForm.value.category
    } as Type, component.image);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

  it('should notify valid save', () => {
    (component.typeService.save as any).and.returnValue(of({}));
    component.typeForm.reset({
      name: 'asd',
      category: 'asd'
    });
    component.save();
    expect(component.typeService.save).toHaveBeenCalledTimes(1);
    expect(component.typeService.save).toHaveBeenCalledWith( {
      name: 'asd',
      category: 'asd'
    } as Type, component.image);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('Type successfully added!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
  });

  it('should render category autocomplete', async () => {
    const el = fixture.debugElement.queryAll(By.css('input'))[1].nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'asd';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.categoryService.filterNames).toHaveBeenCalledTimes(1);
    expect(component.categoryService.filterNames).toHaveBeenCalledWith('asd');
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-option'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe(categoryFilters[0]);
    expect(de[1].nativeElement.textContent.trim()).toBe(categoryFilters[1]);
    expect(de[2].nativeElement.textContent.trim()).toBe(categoryFilters[2]);
  });

});
