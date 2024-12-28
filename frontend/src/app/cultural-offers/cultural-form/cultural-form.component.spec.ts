import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed, } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { of } from 'rxjs';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { TypeService } from 'src/app/cats-types/services/type.service';
import { CulturalValidatorService } from 'src/app/cultural-offers/services/cultural-validator.service';
import { TypeValidatorService } from 'src/app/cats-types/services/type-validator.service';
import { CulturalFormComponent } from './cultural-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { CulturalOffer } from 'src/app/models/cultural-offer';

describe('CulturalFormComponent', () => {
  let component: CulturalFormComponent;
  let fixture: ComponentFixture<CulturalFormComponent>;
  const typeFilters: string[] = ['type1', 'type2', 'type3'];
  const typeValidator: any = {};
  const nameValidator: any = {};
  const locationValidator: any = {};

  beforeEach(async () => {

    const culturalOfferMock = {};
    const culturalServiceMock = {
      save: jasmine.createSpy('save'),
      announceRefreshData: jasmine.createSpy('announceRefreshData'),
      announceMarkOnMap: jasmine.createSpy('announceMarkOnMap')
    };
    const typeServiceMock = {
      filterNames: jasmine.createSpy('filterNames').and.returnValue(of(typeFilters))
    };
    const culturalValidatorMock = {
      hasName: jasmine.createSpy('hasName').and.returnValue(() => of(nameValidator)),
      locationFound: jasmine.createSpy('locationFound').and.returnValue(() => locationValidator)
    };
    const typeValidatorMock = {
      hasName: jasmine.createSpy('hasName').and.returnValue(() => of(typeValidator))
    };
    const dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ CulturalFormComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [MatAutocompleteModule, ReactiveFormsModule],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: culturalOfferMock},
        {provide: CulturalService, useValue: culturalServiceMock},
        {provide: TypeService, useValue: typeServiceMock},
        {provide: CulturalValidatorService, useValue: culturalValidatorMock},
        {provide: TypeValidatorService, useValue: typeValidatorMock},
        {provide: MatDialogRef, useValue: dialogRefMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  afterEach(() => {
    delete typeValidator.nameError;
    delete nameValidator.nameError;
    delete locationValidator.locationError;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalFormComponent);
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
    expect(fixture.debugElement.query(By.css('div')).nativeElement.classList).toContain('disabled');
  });

  it('should recognize empty form', () => {
    component.culturalForm.reset({
      type: '',
      name: '',
      location: '',
      description: ''
    });
    expect(component.culturalForm.valid).toBeFalse();
    expect(component.culturalForm.controls.type.valid).toBeFalse();
    expect(component.culturalForm.controls.name.valid).toBeFalse();
    expect(component.culturalForm.controls.location.valid).toBeFalse();
    expect(component.culturalForm.controls.description.valid).toBeTrue();
    expect(component.culturalForm.controls.type.errors.required).toBeTruthy();
    expect(component.culturalForm.controls.name.errors.required).toBeTruthy();
    expect(component.culturalForm.controls.location.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Type is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[2].nativeElement.textContent.trim()).toBe('Valid location is required!');
  });

  it('should recognize blank form', () => {
    component.culturalForm.reset({
      type: '  ',
      name: '  ',
      location: '  ',
      description: '  '
    });
    expect(component.culturalForm.valid).toBeFalse();
    expect(component.culturalForm.controls.type.valid).toBeFalse();
    expect(component.culturalForm.controls.name.valid).toBeFalse();
    expect(component.culturalForm.controls.location.valid).toBeFalse();
    expect(component.culturalForm.controls.description.valid).toBeTrue();
    expect(component.culturalForm.controls.type.errors.pattern).toBeTruthy();
    expect(component.culturalForm.controls.name.errors.pattern).toBeTruthy();
    expect(component.culturalForm.controls.location.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Type is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[2].nativeElement.textContent.trim()).toBe('Valid location is required!');
  });

  it('should recognize non existing type', () => {
    typeValidator.nameError = true;
    component.culturalForm.reset({
      type: 'asd',
      name: '',
      location: '',
      description: ''
    });
    expect(component.culturalForm.valid).toBeFalse();
    expect(component.culturalForm.controls.type.valid).toBeFalse();
    expect(component.culturalForm.controls.name.valid).toBeFalse();
    expect(component.culturalForm.controls.location.valid).toBeFalse();
    expect(component.culturalForm.controls.description.valid).toBeTrue();
    expect(component.culturalForm.controls.type.errors.nameError).toBeTruthy();
    expect(component.culturalForm.controls.name.errors.required).toBeTruthy();
    expect(component.culturalForm.controls.location.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Type not found!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[2].nativeElement.textContent.trim()).toBe('Valid location is required!');
  });

  it('should recognize taken name', () => {
    nameValidator.nameError = true;
    component.culturalForm.reset({
      type: '',
      name: 'asd',
      location: '',
      description: ''
    });
    expect(component.culturalForm.valid).toBeFalse();
    expect(component.culturalForm.controls.type.valid).toBeFalse();
    expect(component.culturalForm.controls.name.valid).toBeFalse();
    expect(component.culturalForm.controls.location.valid).toBeFalse();
    expect(component.culturalForm.controls.description.valid).toBeTrue();
    expect(component.culturalForm.controls.type.errors.required).toBeTruthy();
    expect(component.culturalForm.controls.name.errors.nameError).toBeTruthy();
    expect(component.culturalForm.controls.location.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Type is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Name already exists!');
    expect(de[2].nativeElement.textContent.trim()).toBe('Valid location is required!');
  });

  it('should recognize invalid location', () => {
    locationValidator.locationError = true;
    component.culturalForm.reset({
      type: '',
      name: '',
      location: 'asd',
      description: ''
    });
    expect(component.culturalForm.valid).toBeFalse();
    expect(component.culturalForm.controls.type.valid).toBeFalse();
    expect(component.culturalForm.controls.name.valid).toBeFalse();
    expect(component.culturalForm.controls.location.valid).toBeFalse();
    expect(component.culturalForm.controls.description.valid).toBeTrue();
    expect(component.culturalForm.controls.type.errors.required).toBeTruthy();
    expect(component.culturalForm.controls.name.errors.required).toBeTruthy();
    expect(component.culturalForm.controls.location.errors.locationError).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toBe('Type is required!');
    expect(de[1].nativeElement.textContent.trim()).toBe('Name is required!');
    expect(de[2].nativeElement.textContent.trim()).toBe('Valid location is required!');
  });

  it('should recognize valid form', () => {
    component.culturalForm.reset({
      type: 'asd',
      name: 'asd',
      location: 'asd',
      description: 'asd'
    });
    expect(component.culturalForm.valid).toBeTrue();
  });

  it('should prevent invalid form submit', () => {
    component.culturalForm.reset({
      type: '',
      name: '',
      location: '',
      description: ''
    });
    component.save();
    expect(component.culturalService.save).not.toHaveBeenCalled();
  });

  it('should notify invalid save', () => {
    (component.culturalService.save as any).and.returnValue(of(null));
    component.culturalForm.reset({
      type: 'asd',
      name: 'asd',
      location: 'asd',
      description: 'asd'
    });
    component.save();
    expect(component.culturalService.save).toHaveBeenCalledTimes(1);
    expect(component.culturalService.save).toHaveBeenCalledWith( {
      id: component.culturalOffer.id,
      type: component.culturalForm.value.type,
      name: component.culturalForm.value.name,
      location: component.culturalForm.value.location,
      lat: component.geolocation.lat,
      lng: component.geolocation.lng,
      description: component.culturalForm.value.description
    } as CulturalOffer, component.image);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

  it('should notify valid save', () => {
    (component.culturalService.save as any).and.returnValue(of({}));
    component.culturalForm.reset({
      type: 'asd',
      name: 'asd',
      location: 'asd',
      description: 'asd'
    });
    component.save();
    expect(component.culturalService.save).toHaveBeenCalledTimes(1);
    expect(component.culturalService.save).toHaveBeenCalledWith( {
      id: component.culturalOffer.id,
      type: component.culturalForm.value.type,
      name: component.culturalForm.value.name,
      location: component.culturalForm.value.location,
      lat: component.geolocation.lat,
      lng: component.geolocation.lng,
      description: component.culturalForm.value.description
    } as CulturalOffer, component.image);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('Offer successfully saved!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith(true);
    expect(component.culturalService.announceRefreshData).toHaveBeenCalledTimes(1);
    expect(component.culturalService.announceRefreshData).toHaveBeenCalledWith({} as CulturalOffer);
    expect(component.culturalService.announceMarkOnMap).toHaveBeenCalledTimes(1);
    expect(component.culturalService.announceMarkOnMap).toHaveBeenCalledWith({} as CulturalOffer);
  });

  it('should render type autocomplete', async () => {
    const el = fixture.debugElement.query(By.css('input')).nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'asd';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.typeService.filterNames).toHaveBeenCalledTimes(1);
    expect(component.typeService.filterNames).toHaveBeenCalledWith('asd');
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-option'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe(typeFilters[0]);
    expect(de[1].nativeElement.textContent.trim()).toBe(typeFilters[1]);
    expect(de[2].nativeElement.textContent.trim()).toBe(typeFilters[2]);
  });

});
