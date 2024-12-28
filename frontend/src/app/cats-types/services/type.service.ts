import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';
import { UniqueCheck } from 'src/app/models/unique-check';
import { Type } from 'src/app/models/type';
import { Image } from 'src/app/models/image';

@Injectable({
  providedIn: 'root'
})
export class TypeService {

  constructor(
    private http: HttpClient
  ) { }

  public readonly API_TYPES = `${environment.baseUrl}/${environment.apiTypes}`;
  private refreshData: Subject<void> = new Subject();
  refreshData$ = this.refreshData.asObservable();

  save(type: Type, image: Image): Observable<Type>{
    return this.http.post<null>(this.API_TYPES, this.typeToFormData(type, image)).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_TYPES}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  hasName(param: UniqueCheck): Observable<boolean>{
    return this.http.post<{value: boolean}>(`${this.API_TYPES}/has_name`, param).pipe(
      map((response: {value: boolean}) => response.value),
      catchError(() => of(false))
    );
  }

  filterNames(filter: string): Observable<string[]>{
    return this.http.post<string[]>(`${this.API_TYPES}/filter_names`, {value: filter}).pipe(
      catchError(() => of([]))
    );
  }

  list(page: number): Observable<HttpResponse<Type[]>>{
    const params = new HttpParams().set('page', page + '').set('size', SMALL_PAGE_SIZE + '');
    return this.http.get<Type[]>(`${this.API_TYPES}`, { observe: 'response', params }).pipe(
      catchError(() => of(null))
    );
  }

  announceRefreshData(): void{
    this.refreshData.next();
  }

  typeToFormData(type: Type, image: Image): FormData{
    const formData: FormData = new FormData();
    formData.append('category', type.category);
    formData.append('name', type.name);
    if (image.upload){
      formData.append('placemarkIcon', image.upload);
    }
    return formData;
  }

}
