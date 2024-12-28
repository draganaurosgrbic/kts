import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';

import { UserService } from './user.service';
import { HttpClient } from '@angular/common/http';
import { Login } from 'src/app/models/login';
import { User } from 'src/app/models/user';
import { ProfileUpdate } from 'src/app/models/profile-update';
import { Image } from 'src/app/models/image';
import { UniqueCheck } from 'src/app/models/unique-check';
import { Registration } from 'src/app/models/registration';

describe('UserService', () => {
  let injector;
  let service: UserService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    injector = getTestBed();
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login valid data', fakeAsync(() => {
    const login: Login = {
      email: 'email1',
      password: 'password1'
    };
    const mockUser: User = {
      id: 1,
      accessToken: 'token1',
      role: 'role1',
      email: 'email1',
      firstName: 'firstName1',
      lastName: 'lastName1',
      image: 'http://localhost:8080/image1'
    };
    let user: User;

    service.login(login).subscribe((res: User) => user = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/login`);
    expect(request.request.method).toBe('POST');
    request.flush(mockUser);
    tick();

    expect(user).toBeDefined();
    expect(user.id).toEqual(mockUser.id);
    expect(user.accessToken).toEqual(mockUser.accessToken);
    expect(user.role).toEqual(mockUser.role);
    expect(user.email).toEqual(mockUser.email);
    expect(user.firstName).toEqual(mockUser.firstName);
    expect(user.lastName).toEqual(mockUser.lastName);
    expect(user.image).toEqual(mockUser.image);
  }));

  it('should not login invalid data', fakeAsync(() => {
    const login: Login = {
      email: 'email1',
      password: 'password1'
    };
    let user: User;

    service.login(login).subscribe((res: User) => user = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/login`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(user).toBeDefined();
    expect(user).toBeNull();
  }));

  it('should register valid data', fakeAsync(() => {
    const registration: Registration = {
      email: 'email1',
      password: 'password1',
      firstName: 'firstName1',
      lastName: 'lastName1'
    };
    let response: boolean;

    service.register(registration).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/register`);
    expect(request.request.method).toBe('POST');
    request.flush({ value: true });
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not register invalid data', fakeAsync(() => {
    const registration: Registration = {
      email: 'email1',
      password: 'password1',
      firstName: 'firstName1',
      lastName: 'lastName1'
    };
    let response: boolean;

    service.register(registration).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/register`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));

  it('should activate valid data', fakeAsync(() => {
    const code = '1';
    let response: boolean;

    service.activate(code).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/activate/${code}`);
    expect(request.request.method).toBe('GET');
    request.flush({ value: true });
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not activate invalid data', fakeAsync(() => {
    const code = '1';
    let response: boolean;

    service.activate(code).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/activate/${code}`);
    expect(request.request.method).toBe('GET');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));


  it('should update valid profile data', fakeAsync(() => {
    const profileUpdate: ProfileUpdate = {
      email: 'email1',
      firstName: 'firstName1',
      lastName: 'lastName1',
      oldPassword: 'oldPassword1',
      newPassword: 'newPassword1',
      newPasswordConfirmation: 'newPasswordConfirmation1'
    };
    const image: Image = {
      path: 'http://localhost:8080/image'
    };
    const mockUser: User = {
      id: 1,
      accessToken: 'token1',
      role: 'role1',
      email: 'email1',
      firstName: 'firstName1',
      lastName: 'lastName1',
      image: 'http://localhost:8080/image1'
    };
    let user: User;

    service.update(profileUpdate, image).subscribe((res: User) => user = res);
    const request: TestRequest = httpMock.expectOne(service.API_USER);
    expect(request.request.method).toBe('POST');
    request.flush(mockUser);
    tick();

    expect(user).toBeDefined();
    expect(user.id).toEqual(mockUser.id);
    expect(user.accessToken).toEqual(mockUser.accessToken);
    expect(user.role).toEqual(mockUser.role);
    expect(user.email).toEqual(mockUser.email);
    expect(user.firstName).toEqual(mockUser.firstName);
    expect(user.lastName).toEqual(mockUser.lastName);
    expect(user.image).toEqual(mockUser.image);
  }));

  it('should not update invalid profile data', fakeAsync(() => {
    const profileUpdate: ProfileUpdate = {
      email: 'email1',
      firstName: 'firstName1',
      lastName: 'lastName1',
      oldPassword: 'oldPassword1',
      newPassword: 'newPassword1',
      newPasswordConfirmation: 'newPasswordConfirmation1'
    };
    const image: Image = {
      path: 'http://localhost:8080/image'
    };
    let user: User;

    service.update(profileUpdate, image).subscribe((res: User) => user = res);
    const request: TestRequest = httpMock.expectOne(service.API_USER);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(user).toBeDefined();
    expect(user).toBeNull();
  }));

  it('should recognize taken email', fakeAsync(() => {
    const param: UniqueCheck = {
      id: 1,
      name: 'name1'
    };
    let response: boolean;

    service.hasEmail(param).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/has_email`);
    expect(request.request.method).toBe('POST');
    request.flush({value: true});
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should recognize free email', fakeAsync(() => {
    const param: UniqueCheck = {
      id: 1,
      name: 'name1'
    };
    let response: boolean;

    service.hasEmail(param).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_AUTH}/has_email`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));

  it('should map profileUpdate to formData', () => {
    const profileUpdate: ProfileUpdate = {
      email: 'email1',
      firstName: 'firstName1',
      lastName: 'lastName1',
      oldPassword: 'oldPassword1',
      newPassword: 'newPassword1',
      newPasswordConfirmation: 'newPasswordConfirmation1'
    };
    const image: Image = {
      path: 'http://localhost:8080/image'
    };
    const formData: FormData = service.profileToFormData(profileUpdate, image);
    expect(formData.get('email')).toEqual(profileUpdate.email);
    expect(formData.get('firstName')).toEqual(profileUpdate.firstName);
    expect(formData.get('lastName')).toEqual(profileUpdate.lastName);
    expect(formData.get('oldPassword')).toEqual(profileUpdate.oldPassword);
    expect(formData.get('newPassword')).toEqual(profileUpdate.newPassword);
    expect(formData.get('newPasswordConfirmation')).toBeFalsy();
    expect(formData.get('imagePath')).toEqual(image.path);
  });

});
