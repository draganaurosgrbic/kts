import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { ADMIN_ROLE, GUEST_ROLE } from 'src/app/constants/roles';
import { LOGIN_PATH, USER_PATH } from 'src/app/constants/router';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';

import { GuestGuard } from './guest.guard';

describe('GuestGuard', () => {
  let guard: GuestGuard;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: 'role1',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1'
  };

  beforeEach(() => {
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock)
    };
    const routerMock = {
      navigate: jasmine.createSpy('navigate')
    };
    TestBed.configureTestingModule({
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    });
    guard = TestBed.inject(GuestGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should permit guest', () => {
    userMock.role = GUEST_ROLE;
    const response: boolean = guard.canActivate();
    expect(response).toBeTrue();
    expect(guard.router.navigate).not.toHaveBeenCalled();
  });

  it('should forbid admin', () => {
    userMock.role = ADMIN_ROLE;
    const response: boolean = guard.canActivate();
    expect(response).toBeFalse();
    expect(guard.router.navigate).toHaveBeenCalledTimes(1);
    expect(guard.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${LOGIN_PATH}`]);
  });

  it('should forbid any other user', () => {
    userMock.role = 'dummy';
    const response: boolean = guard.canActivate();
    expect(response).toBeFalse();
    expect(guard.router.navigate).toHaveBeenCalledTimes(1);
    expect(guard.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${LOGIN_PATH}`]);
  });

});
