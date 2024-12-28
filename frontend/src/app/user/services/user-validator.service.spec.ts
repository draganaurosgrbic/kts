import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AbstractControl, ValidationErrors } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { ValidationError } from 'src/app/models/validation-error';

import { UserService } from 'src/app/user/services/user.service';
import { UserValidatorService } from './user-validator.service';

describe('UserValidatorService', () => {
  let service: UserValidatorService;
  const controlMock: any = {
    parent: {
      get(value: string): any{
        return {value};
      }
    },
    get(value: string): any{
      return {value};
    },
    value: ''
  };

  beforeEach(() => {
    const userServiceMock = {
      hasEmail: jasmine.createSpy('hasEmail')
    };
    TestBed.configureTestingModule({
      providers: [
        {provide: UserService, useValue: userServiceMock}
      ]
    });
    service = TestBed.inject(UserValidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should recognize wrong password confirmation', () => {
    controlMock.value = 'dummy';
    const response: ValidationError = service.passwordConfirmed()(controlMock);
    expect(response).toEqual({passwordError: true});
  });

  it('should recognize right password confirmation', () => {
    controlMock.value = 'password';
    const response: ValidationError = service.passwordConfirmed()(controlMock);
    expect(response).toBeNull();
  });

  it('should recognize no old password', () => {
    controlMock.get = (value: string): any => {
      if (value === 'oldPassword'){
        value = '';
      }
      return {value};
    };
    const response: ValidationError = service.newPasswordConfirmed()(controlMock);
    expect(response).toEqual({oldPasswordError: true});
  });

  it('should recognize wrong new password confirmation', () => {
    controlMock.get = (value: string): any => {
      return {value};
    };
    const response: ValidationError = service.newPasswordConfirmed()(controlMock);
    expect(response).toEqual({newPasswordError: true});
  });

  it('should recognize right new password confirmation', () => {
    controlMock.get = (value: string): any => {
      value = value.replace('Confirmation', '');
      return {value};
    };
    const response: ValidationError = service.newPasswordConfirmed()(controlMock);
    expect(response).toBeNull();
  });

  it('should recognize taken email', fakeAsync(() => {
    let response: ValidationError;
    (service.userService.hasEmail as any).and.returnValue(of(true));
    (service.hasEmail(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toEqual({emailError: true});
  }));

  it('should recognize free email', fakeAsync(() => {
    let response: ValidationError;
    (service.userService.hasEmail as any).and.returnValue(of(false));
    (service.hasEmail(null)({} as AbstractControl) as Observable<ValidationErrors>)
    .subscribe((res: ValidationError) => response = res);
    tick();
    expect(response).toBeNull();
  }));

});
