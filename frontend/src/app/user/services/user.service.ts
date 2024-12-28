import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Login } from 'src/app/models/login';
import { User } from 'src/app/models/user';
import { Registration } from 'src/app/models/registration';
import { UniqueCheck } from 'src/app/models/unique-check';
import { catchError, map } from 'rxjs/operators';
import { ProfileUpdate } from 'src/app/models/profile-update';
import { Image } from 'src/app/models/image';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) { }

  readonly API_AUTH: string = `${environment.baseUrl}/${environment.apiAuth}`;
  readonly API_USER: string = `${environment.baseUrl}/${environment.apiUser}`;

  login(login: Login): Observable<User>{
    return this.http.post<User>(`${this.API_AUTH}/login`, login).pipe(
      catchError(() => of(null))
    );
  }

  register(register: Registration): Observable<boolean>{
    return this.http.post<null>(`${this.API_AUTH}/register`, register).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  activate(code: string): Observable<boolean>{
    return this.http.get<null>(`${this.API_AUTH}/activate/${code}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  update(profileUpdate: ProfileUpdate, image: Image): Observable<User>{
    return this.http.post<User>(this.API_USER, this.profileToFormData(profileUpdate, image)).pipe(
      catchError(() => of(null))
    );
  }

  hasEmail(param: UniqueCheck): Observable<boolean>{
    return this.http.post<{value: boolean}>(`${this.API_AUTH}/has_email`, param).pipe(
      map((response: {value: boolean}) => response.value),
      catchError(() => of(false))
    );
  }

  profileToFormData(profileUpdate: ProfileUpdate, image: Image): FormData{
    const formData: FormData = new FormData();
    for (const key in profileUpdate){
      if (key === 'oldPassword' && !profileUpdate.newPassword){
        continue;
      }
      if (key === 'newPassword' && !profileUpdate.newPassword){
        continue;
      }
      if (key === 'newPasswordConfirmation'){
        continue;
      }
      formData.append(key, profileUpdate[key]);
    }

    if (image.upload){
      formData.append('image', image.upload);
    }
    else if (image.path){
      formData.append('imagePath', image.path);
    }
    return formData;
  }

}
