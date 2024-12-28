import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { LARGE_PAGE_SIZE } from 'src/app/constants/pagination';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { FilterParams } from 'src/app/models/filter-params';
import { Image } from 'src/app/models/image';
import { UniqueCheck } from 'src/app/models/unique-check';
import { environment } from 'src/environments/environment';
import { RateUpdate } from '../../models/rate-update';

@Injectable({
  providedIn: 'root'
})
export class CulturalService {

  constructor(
    private http: HttpClient
  ) { }

  readonly API_OFFERS = `${environment.baseUrl}/${environment.apiCulturalOffers}`;
  private refreshData: Subject<CulturalOffer | number> = new Subject();
  private filterData: Subject<FilterParams> = new Subject();
  private markOnMap: Subject<CulturalOffer> = new Subject();
  private updateTotalRate: Subject<RateUpdate> = new Subject();

  refreshData$: Observable<CulturalOffer | number> = this.refreshData.asObservable();
  filterData$: Observable<FilterParams> = this.filterData.asObservable();
  markOnMap$: Observable<CulturalOffer> = this.markOnMap.asObservable();
  updateTotalRate$: Observable<RateUpdate> = this.updateTotalRate.asObservable();

  save(culturalOffer: CulturalOffer, image: Image): Observable<CulturalOffer>{
    return this.http.post<CulturalOffer>(this.API_OFFERS, this.offerToFormData(culturalOffer, image)).pipe(
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<boolean>{
    return this.http.delete<null>(`${this.API_OFFERS}/${id}`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  hasName(param: UniqueCheck): Observable<boolean>{
    return this.http.post<{value: boolean}>(`${this.API_OFFERS}/has_name`, param).pipe(
      map((response: {value: boolean}) => response.value),
      catchError(() => of(false))
    );
  }

  filterNames(filter: string): Observable<string[]>{
    return this.http.post<string[]>(`${this.API_OFFERS}/filter_names`, {value: filter}).pipe(
      catchError(() => of([]))
    );
  }

  filterLocations(filter: string): Observable<string[]>{
    return this.http.post<string[]>(`${this.API_OFFERS}/filter_locations`, {value: filter}).pipe(
      catchError(() => of([]))
    );
  }

  filterTypes(filter: string): Observable<string[]>{
    return this.http.post<string[]>(`${this.API_OFFERS}/filter_types`, {value: filter}).pipe(
      catchError(() => of([]))
    );
  }

  filter(filters: FilterParams, page: number): Observable<HttpResponse<CulturalOffer[]>>{
    const params = new HttpParams().set('page', page + '').set('size', LARGE_PAGE_SIZE + '');
    return this.http.post<CulturalOffer[]>(`${this.API_OFFERS}/filter`, filters, { observe: 'response', params }).pipe(
      catchError(() => of(null))
    );
  }

  announceRefreshData(param: CulturalOffer | number): void{
    this.refreshData.next(param);
  }
  announceFilterData(filterParams: FilterParams): void{
    this.filterData.next(filterParams);
  }
  announceMarkOnMap(culturalOffer: CulturalOffer): void{
    this.markOnMap.next(culturalOffer);
  }
  announceUpdateTotalRate(rateUpdate: RateUpdate): void{
    this.updateTotalRate.next(rateUpdate);
  }

  offerToFormData(culturalOffer: CulturalOffer, image: Image): FormData{
    const formData: FormData = new FormData();
    for (const key in culturalOffer){
      if (culturalOffer[key] === undefined || culturalOffer[key] === null){
        continue;
      }
      formData.append(key, culturalOffer[key]);
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
