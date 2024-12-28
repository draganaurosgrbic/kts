import { TestBed } from '@angular/core/testing';
import { User } from 'src/app/models/user';

import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
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
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save user', () => {
    service.saveUser(userMock);
    const user: User = JSON.parse(localStorage.getItem(service.STORAGE_KEY));
    expect(user).toBeTruthy();
    expect(user.id).toBe(userMock.id);
    expect(user.accessToken).toBe(userMock.accessToken);
    expect(user.role).toBe(userMock.role);
    expect(user.email).toBe(userMock.email);
    expect(user.firstName).toBe(userMock.firstName);
    expect(user.lastName).toBe(userMock.lastName);
    expect(user.image).toBe(userMock.image);
  });

  it('should delete user', () => {
    service.deleteUser();
    expect(localStorage.getItem('user')).toBeFalsy();
  });

  it('should get user', () => {
    localStorage.setItem(service.STORAGE_KEY, JSON.stringify(userMock));
    const user: User = service.getUser();
    expect(user).toBeTruthy();
    expect(user.id).toBe(userMock.id);
    expect(user.accessToken).toBe(userMock.accessToken);
    expect(user.role).toBe(userMock.role);
    expect(user.email).toBe(userMock.email);
    expect(user.firstName).toBe(userMock.firstName);
    expect(user.lastName).toBe(userMock.lastName);
    expect(user.image).toBe(userMock.image);
  });

});
