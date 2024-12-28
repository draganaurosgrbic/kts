import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';
import { Category } from 'src/app/models/category';
import { UniqueCheck } from 'src/app/models/unique-check';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(
    private http: HttpClient
  ) { }

  public readonly API_CATEGORIES: string = `${environment.baseUrl}/${environment.apiCategories}`;
  private refreshData: Subject<void> = new Subject();
  refreshData$ = this.refreshData.asObservable();

  save(category: Category): Observable<Category>{
    return this.http.post<null>(this.API_CATEGORIES, category).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_CATEGORIES}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  hasName(param: UniqueCheck): Observable<boolean>{
    return this.http.post<{value: boolean}>(`${this.API_CATEGORIES}/has_name`, param).pipe(
      map((response: {value: boolean}) => response.value),
      catchError(() => of(false))
    );
  }

  filterNames(filter: string): Observable<string[]>{
    return this.http.post<string[]>(`${this.API_CATEGORIES}/filter_names`, {value: filter}).pipe(
      catchError(() => of([]))
    );
  }

  list(page: number): Observable<HttpResponse<Category[]>>{
    const params = new HttpParams().set('page', page + '').set('size', SMALL_PAGE_SIZE + '');
    return this.http.get<Category[]>(this.API_CATEGORIES, { observe: 'response', params}).pipe(
      catchError(() => of(null))
    );
  }

  announceRefreshData(): void{
    this.refreshData.next();
  }

}
