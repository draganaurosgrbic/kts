import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { of } from 'rxjs';
import { ProfileFormComponent } from './profile-form.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/user/services/user.service';
import { UserValidatorService } from 'src/app/user/services/user-validator.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { User } from 'src/app/models/user';
import { ProfileUpdate } from 'src/app/models/profile-update';

describe('ProfileFormComponent', () => {
  let component: ProfileFormComponent;
  let fixture: ComponentFixture<ProfileFormComponent>;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: 'role1',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1'
  };
  const emailValidator: any = {};
  const passwordValidator: any = {};

  beforeEach(async () => {
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock),
      saveUser: jasmine.createSpy('saveUser')
    };
    const userServiceMock = {
      update: jasmine.createSpy('update')
    };
    const userValidatorMock = {
      hasEmail: jasmine.createSpy('hasEmail').and.returnValue(() => of(emailValidator)),
      newPasswordConfirmed: jasmine.createSpy('newPasswordConfirmed').and.returnValue(() => passwordValidator)
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ ProfileFormComponent ],
      imports: [ReactiveFormsModule, FormsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: UserService, useValue: userServiceMock},
        {provide: UserValidatorService, useValue: userValidatorMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileFormComponent);
    component = fixture.componentInstance;
    component.savePending = false;
    fixture.detectChanges();
  });

  afterEach(() => {
    delete emailValidator.emailError;
    delete passwordValidator.oldPasswordError;
    delete passwordValidator.newPasswordError;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.savePending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('div')).nativeElement.classList).toContain('disabled');
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should recognize empty form', () => {
    component.profileForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      oldPassword: '',
      newPassword: '',
      newPasswordConfirmation: ''
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.required).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
  });

  it('should recognize blank form', () => {
    component.profileForm.reset({
      email: '  ',
      firstName: '  ',
      lastName: '  ',
      oldPassword: '  ',
      newPassword: '  ',
      newPasswordConfirmation: '  '
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.pattern).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.pattern).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
  });

  it('should recognize invalid email', () => {
    component.profileForm.reset({
      email: 'dummy',
      firstName: '',
      lastName: '',
      oldPassword: '',
      newPassword: '',
      newPasswordConfirmation: ''
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.pattern).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
  });

  it('should recognize taken email', () => {
    emailValidator.emailError = true;
    component.profileForm.reset({
      email: 'dummy@gmail.com',
      firstName: '',
      lastName: '',
      oldPassword: '',
      newPassword: '',
      newPasswordConfirmation: ''
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.emailError).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Email already exists!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
  });

  it('should recognize no old password when new password', () => {
    passwordValidator.oldPasswordError = true;
    component.profileForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      oldPassword: '',
      newPassword: 'dummy',
      newPasswordConfirmation: ''
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.required).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.required).toBeTruthy();
    expect(component.profileForm.errors.oldPasswordError).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[3].nativeElement.textContent.trim()).toEqual('Old password is required!');
  });

  it('should recognize no old password when confirmation password', () => {
    passwordValidator.oldPasswordError = true;
    component.profileForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      oldPassword: '',
      newPassword: '',
      newPasswordConfirmation: 'dummy'
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.required).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.required).toBeTruthy();
    expect(component.profileForm.errors.oldPasswordError).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[3].nativeElement.textContent.trim()).toEqual('Old password is required!');
  });

  it('should recognize wrong password confirmation', () => {
    passwordValidator.newPasswordError = true;
    component.profileForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      oldPassword: 'dummy',
      newPassword: 'dummy',
      newPasswordConfirmation: 'dummy2'
    });
    expect(component.profileForm.valid).toBeFalse();
    expect(component.profileForm.controls.email.valid).toBeFalse();
    expect(component.profileForm.controls.firstName.valid).toBeFalse();
    expect(component.profileForm.controls.lastName.valid).toBeFalse();
    expect(component.profileForm.controls.email.errors.required).toBeTruthy();
    expect(component.profileForm.controls.firstName.errors.required).toBeTruthy();
    expect(component.profileForm.controls.lastName.errors.required).toBeTruthy();
    expect(component.profileForm.errors.newPasswordError).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Valid email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('First name is required!');
    expect(de[2].nativeElement.textContent.trim()).toEqual('Last name is required!');
    expect(de[3].nativeElement.textContent.trim()).toEqual('Passwords do not match!');
  });

  it('should recognize valid form', () => {
    component.profileForm.reset({
      email: 'dummy@gmail.com',
      firstName: 'dummy',
      lastName: 'dummy',
      oldPassword: 'dummy',
      newPassword: 'dummy',
      newPasswordConfirmation: 'dummy'
    });
    expect(component.profileForm.valid).toBeTrue();
  });

  it('should prevent invalid form submit', () => {
    component.profileForm.reset({
      email: '',
      firstName: '',
      lastName: '',
      oldPassword: '',
      newPassword: '',
      newPasswordConfirmation: ''
    });
    component.save();
    expect(component.userService.update).not.toHaveBeenCalled();
  });

  it('should notify invalid profile update', () => {
    (component.userService.update as any).and.returnValue(of(null));
    component.profileForm.reset({
      email: 'dummy@gmail.com',
      firstName: 'dummy',
      lastName: 'dummy',
      oldPassword: 'dummy',
      newPassword: 'dummy',
      newPasswordConfirmation: 'dummy'
    });
    const value: ProfileUpdate = component.profileForm.value;
    component.save();
    expect(component.userService.update).toHaveBeenCalledTimes(1);
    expect(component.userService.update).toHaveBeenCalledWith(value, component.image);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

  it('should notify valid profile update', () => {
    (component.userService.update as any).and.returnValue(of({}));
    component.profileForm.reset({
      email: 'dummy@gmail.com',
      firstName: 'dummy',
      lastName: 'dummy',
      oldPassword: 'dummy',
      newPassword: 'dummy',
      newPasswordConfirmation: 'dummy'
    });
    const value: ProfileUpdate = component.profileForm.value;
    component.save();
    expect(component.userService.update).toHaveBeenCalledTimes(1);
    expect(component.userService.update).toHaveBeenCalledWith(value, component.image);
    expect(component.authService.saveUser).toHaveBeenCalledTimes(1);
    expect(component.authService.saveUser).toHaveBeenCalledWith({} as User);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('Your profile has been updated!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
  });

});
