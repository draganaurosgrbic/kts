import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';
import { Image } from 'src/app/models/image';
import { News } from 'src/app/models/news';
import { NewsFilterParams } from 'src/app/models/news-filter-params';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(
    private http: HttpClient
  ) { }

  readonly API_NEWS = `${environment.baseUrl}/${environment.apiNews}`;
  readonly API_OFFERS = `${environment.baseUrl}/${environment.apiCulturalOffers}`;
  private refreshData: Subject<number> = new Subject();
  refreshData$ = this.refreshData.asObservable();

  save(news: News, images: Image[]): Observable<News>{
    return this.http.post<News>(this.API_NEWS, this.newsToFormData(news, images)).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_NEWS}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  announceRefreshData(culturalOfferId: number): void{
    this.refreshData.next(culturalOfferId);
  }

  filter(filters: NewsFilterParams, culturalOfferId: number, page: number): Observable<HttpResponse<News[]>>{
    const params = new HttpParams().set('page', page + '').set('size', SMALL_PAGE_SIZE + '');
    return this.http.post<News[]>(`${this.API_OFFERS}/${culturalOfferId}/filter_news`, filters, {observe: 'response', params}).pipe(
      catchError(() => of(null))
    );
  }

  newsToFormData(news: News, images: Image[]): FormData{
    const formData: FormData = new FormData();
    if (news.id){
      formData.append('id', news.id + '');
    }
    formData.append('culturalOfferId', news.culturalOfferId + '');
    formData.append('text', news.text);

    for (const image of images){
      if (image.upload){
        formData.append('images', image.upload);
      }
      else if (image.path){
        formData.append('imagePaths', image.path);
      }
    }
    return formData;

  }

}
