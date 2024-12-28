import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { LARGE_PAGE_SIZE } from 'src/app/constants/pagination';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { FilterParams } from 'src/app/models/filter-params';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserFollowingService {

  constructor(
    private http: HttpClient
  ) { }

  readonly API_OFFERS = `${environment.baseUrl}/${environment.apiCulturalOffers}`;

  filter(filters: FilterParams, page: number): Observable<HttpResponse<CulturalOffer[]>>{
    const params = new HttpParams().set('page', page + '').set('size', LARGE_PAGE_SIZE + '');
    return this.http.post<CulturalOffer[]>(`${this.API_OFFERS}/filter_followings`, filters, {observe: 'response', params}).pipe(
      catchError(() => of(null))
    );
  }

  toggleSubscription(culturalOfferId: number): Observable<boolean>{
    return this.http.get<null>(`${this.API_OFFERS}/${culturalOfferId}/toggle_subscription`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

}
