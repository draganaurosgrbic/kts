import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { UserService } from 'src/app/user/services/user.service';
import { UserValidatorService } from 'src/app/user/services/user-validator.service';
import { RegisterFormComponent } from './register-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Registration } from 'src/app/models/registration';
import { SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';

describe('RegisterFormComponent', () => {
  let component: RegisterFormComponent;
  let fixture: ComponentFixture<RegisterFormComponent>;
  const emailValidator: any = {};
  const passwordValidator: any = {};

  beforeEach(async () => {
    const userServiceMock = {
      register: jasmine.createSpy('register')
    };
    const formValidatorMock = {
      hasEmail: jasmine.createSpy('hasEmail').and.returnValue(() => of(emailValidator)),
      passwordConfirmed: jasmine.createSpy('passwordConfirmed').and.returnValue(() => passwordValidator)
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ RegisterFormComponent ],
      imports: [ReactiveFormsModule, FormsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: UserService, useValue: userServiceMock},
        {provide: UserValidatorService, useValue: formValidatorMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterFormComponent);
    component = fixture.componentInstance;
    component.registerPending = false;
    fixture.detectChanges();
  });

  afterEach(() => {
    delete emailValidator.emailError;
    delete passwordValidator.passwordError;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.registerPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('div')).nativeElement.classList).toContain('disabled');
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should recognize empty form', () => {
    component.registerForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      passwordConfirmation: ''
    });
    expect(component.registerForm.valid).toBeFalse();
    expect(component.registerForm.controls.email.valid).toBeFalse();
    expect(component.registerForm.controls.firstName.valid).toBeFalse();
    expect(component.registerForm.controls.lastName.valid).toBeFalse();
    expect(component.registerForm.controls.email.errors.required).toBeTruthy();
    expect(component.registerForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.registerForm.controls.lastName.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Valid email is required!');
  });

  it('should recognize blank form', () => {
    component.registerForm.reset({
      email: '  ',
      firstName: '  ',
      lastName: '  ',
      password: '  ',
      passwordConfirmation: '  '
    });
    expect(component.registerForm.valid).toBeFalse();
    expect(component.registerForm.controls.email.valid).toBeFalse();
    expect(component.registerForm.controls.firstName.valid).toBeFalse();
    expect(component.registerForm.controls.lastName.valid).toBeFalse();
    expect(component.registerForm.controls.email.errors.pattern).toBeTruthy();
    expect(component.registerForm.controls.firstName.errors.pattern).toBeTruthy();
    expect(component.registerForm.controls.lastName.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Valid email is required!');
  });

  it('should recognize invalid email', () => {
    component.registerForm.reset({
      email: 'dummy',
      firstName: '',
      lastName: '',
      password: '',
      passwordConfirmation: ''
    });
    expect(component.registerForm.valid).toBeFalse();
    expect(component.registerForm.controls.email.valid).toBeFalse();
    expect(component.registerForm.controls.firstName.valid).toBeFalse();
    expect(component.registerForm.controls.lastName.valid).toBeFalse();
    expect(component.registerForm.controls.email.errors.pattern).toBeTruthy();
    expect(component.registerForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.registerForm.controls.lastName.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Valid email is required!');
  });

  it('should recognize taken email', () => {
    emailValidator.emailError = true;
    component.registerForm.reset({
      email: 'dummy@gmail.com',
      firstName: '',
      lastName: '',
      password: '',
      passwordConfirmation: ''
    });
    expect(component.registerForm.valid).toBeFalse();
    expect(component.registerForm.controls.email.valid).toBeFalse();
    expect(component.registerForm.controls.firstName.valid).toBeFalse();
    expect(component.registerForm.controls.lastName.valid).toBeFalse();
    expect(component.registerForm.controls.email.errors.emailError).toBeTruthy();
    expect(component.registerForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.registerForm.controls.lastName.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Email already exists!');
  });

  it('should recognize wrong password confirmation', () => {
    passwordValidator.passwordError = true;
    component.registerForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      password: 'dummy',
      passwordConfirmation: 'dummy2'
    });
    expect(component.registerForm.valid).toBeFalse();
    expect(component.registerForm.controls.email.valid).toBeFalse();
    expect(component.registerForm.controls.firstName.valid).toBeFalse();
    expect(component.registerForm.controls.lastName.valid).toBeFalse();
    expect(component.registerForm.controls.email.errors.required).toBeTruthy();
    expect(component.registerForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.registerForm.controls.lastName.errors.required).toBeTruthy();
    expect(component.registerForm.controls.passwordConfirmation.errors.passwordError).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[4].nativeElement.textContent.trim()).toEqual('Passwords do not match!');
  });

  it('should recognize valid form', () => {
    component.registerForm.reset({
      email: 'dummy@gmail.com',
      firstName: 'dummy',
      lastName: 'dummy',
      password: 'dummy',
      passwordConfirmation: 'dummy'
    });
    expect(component.registerForm.valid).toBeTrue();
  });

  it('should prevent invalid form submit', () => {
    component.registerForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      passwordConfirmation: ''
    });
    component.register();
    expect(component.userService.register).not.toHaveBeenCalled();
  });

  it('should notify valid form submit', () => {
    (component.userService.register as any).and.returnValue(of({}));
    component.registerForm.reset({
      email: 'dummy@gmail.com',
      firstName: 'dummy',
      lastName: 'dummy',
      password: 'dummy',
      passwordConfirmation: 'dummy'
    });
    const value: Registration = component.registerForm.value;
    component.register();
    expect(component.userService.register).toHaveBeenCalledTimes(1);
    expect(component.userService.register).toHaveBeenCalledWith(value);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('Your request has been sent! Check your email.',
    SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
  });


});
