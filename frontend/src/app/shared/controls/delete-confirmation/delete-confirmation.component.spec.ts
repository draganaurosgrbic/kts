import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { DELETE_ERROR, DELETE_SUCCESS, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { DeleteConfirmationComponent } from './delete-confirmation.component';

describe('DeleteConfirmationComponent', () => {
  let component: DeleteConfirmationComponent;
  let fixture: ComponentFixture<DeleteConfirmationComponent>;

  beforeEach(async () => {
    const deleteFunctionMock = jasmine.createSpy();
    const dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    const snackbarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ DeleteConfirmationComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: deleteFunctionMock},
        {provide: MatDialogRef, useValue: dialogRefMock},
        {provide: MatSnackBar, useValue: snackbarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteConfirmationComponent);
    component = fixture.componentInstance;
    component.deletePending = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabed when pending', () => {
    component.deletePending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should close dialog when cancelled', () => {
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith();
  });

  it('should notify valid delete when true boolean', () => {
    component.deleteFunction = jasmine.createSpy().and.returnValue(of(true));
    component.confirm();
    expect(component.deleteFunction).toHaveBeenCalledTimes(1);
    expect(component.deleteFunction).toHaveBeenCalledWith();
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith(true);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(DELETE_SUCCESS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
  });

  it('should notify valid delete when number', () => {
    component.deleteFunction = jasmine.createSpy().and.returnValue(of(0));
    component.confirm();
    expect(component.deleteFunction).toHaveBeenCalledTimes(1);
    expect(component.deleteFunction).toHaveBeenCalledWith();
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith(0);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(DELETE_SUCCESS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
  });

  it('should notify invalid delete when false boolean', () => {
    component.deleteFunction = jasmine.createSpy().and.returnValue(of(false));
    component.confirm();
    expect(component.deleteFunction).toHaveBeenCalledTimes(1);
    expect(component.deleteFunction).toHaveBeenCalledWith();
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(DELETE_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

  it('should notify invalid delete when null', () => {
    component.deleteFunction = jasmine.createSpy().and.returnValue(of(null));
    component.confirm();
    expect(component.deleteFunction).toHaveBeenCalledTimes(1);
    expect(component.deleteFunction).toHaveBeenCalledWith();
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(DELETE_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

});
