import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS } from 'src/app/constants/snackbar';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/user/services/user.service';
import { LoginFormComponent } from './login-form.component';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;

  beforeEach(async () => {
    const authServiceMock = {
      saveUser: jasmine.createSpy('saveUser')
    };
    const userServiceMock = {
      login: jasmine.createSpy('login')
    };
    const routerMock = {
      navigate: jasmine.createSpy('navigate')
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ LoginFormComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [ReactiveFormsModule, FormsModule],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: UserService, useValue: userServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    component.loginPending = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.loginPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('div')).nativeElement.classList).toContain('disabled');
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should recognize empty form', () => {
    component.loginForm.reset({
      email: '',
      password: ''
    });
    expect(component.loginForm.invalid).toBeTrue();
    expect(component.loginForm.controls.email.invalid).toBeTrue();
    expect(component.loginForm.controls.password.invalid).toBeTrue();
    expect(component.loginForm.controls.email.errors.required).toBeTruthy();
    expect(component.loginForm.controls.password.errors.required).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('Email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Password is required!');
  });

  it('should recognize blank form', () => {
    component.loginForm.reset({
      email: '  ',
      password: '  '
    });
    expect(component.loginForm.invalid).toBeTrue();
    expect(component.loginForm.controls.email.invalid).toBeTrue();
    expect(component.loginForm.controls.password.invalid).toBeTrue();
    expect(component.loginForm.controls.email.errors.pattern).toBeTruthy();
    expect(component.loginForm.controls.password.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-error'));
    expect(de.length).toEqual(2);
    expect(de[0].nativeElement.textContent.trim()).toEqual('Email is required!');
    expect(de[1].nativeElement.textContent.trim()).toEqual('Password is required!');
  });

  it('should recognize valid form', () => {
    component.loginForm.reset({
      email: 'asd',
      password: 'asd'
    });
    expect(component.loginForm.valid).toBeTrue();
  });

  it('should prevent invalid form submit', () => {
    component.loginForm.reset({
      email: '',
      password: ''
    });
    component.login();
    expect(component.userService.login).not.toHaveBeenCalled();
  });

  it('should notify invalid login', () => {
    (component.userService.login as any).and.returnValue(of(null));
    component.loginForm.reset({
      email: 'asd',
      password: 'asd'
    });
    component.login();
    expect(component.userService.login).toHaveBeenCalledTimes(1);
    expect(component.userService.login).toHaveBeenCalledWith(component.loginForm.value);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  });

  it('should notify valid login', () => {
    (component.userService.login as any).and.returnValue(of({}));
    component.loginForm.reset({
      email: 'asd',
      password: 'asd'
    });
    component.login();
    expect(component.userService.login).toHaveBeenCalledTimes(1);
    expect(component.userService.login).toHaveBeenCalledWith(component.loginForm.value);
    expect(component.authService.saveUser).toHaveBeenCalledTimes(1);
    expect(component.authService.saveUser).toHaveBeenCalledWith({} as User);
    expect(component.router.navigate).toHaveBeenCalledTimes(1);
    expect(component.router.navigate).toHaveBeenCalledWith(['/']);
  });

});
