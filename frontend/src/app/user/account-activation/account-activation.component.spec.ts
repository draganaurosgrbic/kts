import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { UserService } from 'src/app/user/services/user.service';
import { AccountActivationComponent } from './account-activation.component';

describe('AccountActivationComponent', () => {
  let component: AccountActivationComponent;
  let fixture: ComponentFixture<AccountActivationComponent>;
  const code = '1';

  beforeEach(async () => {
    const userServiceMock = {
      activate: jasmine.createSpy('activate').and.returnValue(of(true))
    };
    const routerMock = {
      navigate: jasmine.createSpy('navigate')
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    const routeMock = {
      snapshot: {
        params: {
          code
        }
      }
    };
    await TestBed.configureTestingModule({
      declarations: [ AccountActivationComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: UserService, useValue: userServiceMock},
        {provide: ActivatedRoute, useValue: routeMock},
        {provide: Router, useValue: routerMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountActivationComponent);
    component = fixture.componentInstance;
    component.activatePending = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.userService.activate).toHaveBeenCalledTimes(1);
    expect(component.userService.activate).toHaveBeenCalledWith(code);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('Your account has been activated! You can login now.',
    SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
    expect(component.router.navigate).toHaveBeenCalledTimes(1);
    expect(component.router.navigate).toHaveBeenCalledWith(['user/login']);
  });
});
