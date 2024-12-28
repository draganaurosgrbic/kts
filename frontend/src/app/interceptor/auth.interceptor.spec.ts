import { HttpRequest, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { fakeAsync, TestBed } from '@angular/core/testing';
import { environment } from 'src/environments/environment';
import { AuthInterceptor } from './auth.interceptor';
import { User } from '../models/user';

describe('AuthInterceptor', () => {
  let authInterceptor: AuthInterceptor;

  const user: User = {
    id: 1,
    accessToken: 'token1',
    role: 'role1',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthInterceptor
      ]
    });
    authInterceptor = TestBed.inject(AuthInterceptor);
  });

  it('should be created', () => {
    expect(authInterceptor).toBeTruthy();
  });

  it('should set header authorization', fakeAsync(() => {
    spyOn(authInterceptor.authService, 'getUser').and.returnValue(user);

    let response: HttpResponse<any>;

    const next: any = {
      handle: responseHandle => {
        response = responseHandle;
      }
    };

    const request: HttpRequest<any> = new HttpRequest<any>('GET', `${environment.baseUrl}/api`);

    authInterceptor.intercept(request, next);
    expect(authInterceptor.authService.getUser).toHaveBeenCalledTimes(1);
    expect(response.headers.get('Authorization')).toEqual(user.accessToken);
  }));

  it('should not set header authorization when user is null', fakeAsync(() => {
    spyOn(authInterceptor.authService, 'getUser').and.returnValue(null);

    let response: HttpResponse<any>;

    const next: any = {
      handle: responseHandle => {
        response = responseHandle;
      }
    };

    const request: HttpRequest<any> = new HttpRequest<any>('GET', `${environment.baseUrl}/api`);

    authInterceptor.intercept(request, next);
    expect(authInterceptor.authService.getUser).toHaveBeenCalledTimes(1);
    expect(response.headers.get('Authorization')).toBeFalsy();
  }));

  it('should not set header authorization when user is undefined', fakeAsync(() => {
    spyOn(authInterceptor.authService, 'getUser').and.returnValue(undefined);

    let response: HttpResponse<any>;

    const next: any = {
      handle: responseHandle => {
        response = responseHandle;
      }
    };

    const request: HttpRequest<any> = new HttpRequest<any>('GET', `${environment.baseUrl}/api`);

    authInterceptor.intercept(request, next);
    expect(authInterceptor.authService.getUser).toHaveBeenCalledTimes(1);
    expect(response.headers.get('Authorization')).toBeFalsy();
  }));

});
