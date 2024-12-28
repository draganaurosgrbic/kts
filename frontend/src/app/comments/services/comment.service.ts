import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { Comment } from 'src/app/models/comment';
import { Image } from 'src/app/models/image';
import { RateUpdate } from 'src/app/models/rate-update';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(
    private http: HttpClient,
    public culturalService: CulturalService
  ) { }

  readonly API_COMMENTS = `${environment.baseUrl}/${environment.apiComments}`;
  readonly API_OFFERS = `${environment.baseUrl}/${environment.apiCulturalOffers}`;
  private refreshData: Subject<number> = new Subject();
  refreshData$: Observable<number> = this.refreshData.asObservable();

  save(comment: Comment, images: Image[]): Observable<number>{
    return this.http.post<{value: number}>(this.API_COMMENTS, this.commentToFormData(comment, images)).pipe(
      map((response: {value: number}) => response.value),
      catchError(() => of(null))
    );
  }

  delete(id: number): Observable<number>{
    return this.http.delete<{value: number}>(`${this.API_COMMENTS}/${id}`).pipe(
      map((response: {value: number}) => response.value),
      catchError(() => of(null))
    );
  }

  list(culturalOfferId: number, page: number): Observable<HttpResponse<Comment[]>>{
    const params = new HttpParams().set('page', page + '').set('size', SMALL_PAGE_SIZE + '');
    return this.http.get<Comment[]>(`${this.API_OFFERS}/${culturalOfferId}/comments`, {observe: 'response', params}).pipe(
      catchError(() => of(null))
    );
  }

  announceRefreshData(rateUpdate: RateUpdate): void{
    this.refreshData.next(rateUpdate.id);
    this.culturalService.announceUpdateTotalRate(rateUpdate);
  }

  commentToFormData(comment: Comment, images: Image[]): FormData{
    const formData: FormData = new FormData();
    for (const key in comment){
      if (comment[key] === undefined || comment[key] === null){
        continue;
      }
      formData.append(key, comment[key]);
    }
    for (const image of images){
      if (image.upload){
        formData.append('images', image.upload);
      }
      else if (image.path) {
        formData.append('imagePaths', image.path);
      }
    }
    return formData;
  }

}
