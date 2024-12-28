import { HttpClient, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { Comment } from 'src/app/models/comment';
import { Image } from 'src/app/models/image';
import { RateUpdate } from 'src/app/models/rate-update';

import { CommentService } from './comment.service';

describe('CommentService', () => {
  let injector;
  let service: CommentService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    const culturalServiceMock = {
      announceUpdateTotalRate: jasmine.createSpy('announceUpdateTotalRate')
    };
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {provide: CulturalService, useValue: culturalServiceMock}
      ]
    });
    injector = getTestBed();
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save valid comment', fakeAsync(() => {
    const comment: Comment = {
      id: 1,
      culturalOfferId: 1,
      rate: 1,
      text: 'some text',
    } as Comment;
    const images: Image[] = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];
    const mockOfferRate = 5;
    let offerRate;

    service.save(comment, images).subscribe((res: number) => offerRate = res);
    const request: TestRequest = httpMock.expectOne(service.API_COMMENTS);
    expect(request.request.method).toBe('POST');
    request.flush({value: mockOfferRate});
    tick();

    expect(offerRate).toBeDefined();
    expect(offerRate).toEqual(mockOfferRate);
  }));

  it('should not save invalid comment', fakeAsync(() => {
    const comment: Comment = {
      id: 1,
      culturalOfferId: 1,
      rate: 1,
      text: 'some text',
    } as Comment;
    const images: Image[] = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];
    let offerRate;

    service.save(comment, images).subscribe((res: number) => offerRate = res);
    const request: TestRequest = httpMock.expectOne(service.API_COMMENTS);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(offerRate).toBeDefined();
    expect(offerRate).toBeNull();
  }));

  it('should delete valid comment', fakeAsync(() => {
    const comment: Comment = {
      id: 1,
      culturalOfferId: 1,
      rate: 1,
      text: 'some text',
    } as Comment;
    const mockOfferRate = 5;
    let offerRate;

    service.delete(comment.id).subscribe((res: number) => offerRate = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_COMMENTS}/${comment.id}`);
    expect(request.request.method).toBe('DELETE');
    request.flush({value: mockOfferRate});
    tick();

    expect(offerRate).toBeDefined();
    expect(offerRate).toEqual(mockOfferRate);
  }));

  it('should not delete invalid comment', fakeAsync(() => {
    const comment: Comment = {
      id: 1,
      culturalOfferId: 1,
      rate: 1,
      text: 'some text',
    } as Comment;
    let offerRate;

    service.delete(comment.id).subscribe((res: number) => offerRate = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_COMMENTS}/${comment.id}`);
    expect(request.request.method).toBe('DELETE');
    request.error(null);
    tick();

    expect(offerRate).toBeDefined();
    expect(offerRate).toBeNull();
  }));

  it('should list no comments', fakeAsync(() => {
    const culturalOfferId = 1;
    const page = 0;
    let comments: Comment[];

    service.list(culturalOfferId, page).subscribe((res: HttpResponse<Comment[]>) => comments = res as null);
    const request: TestRequest = httpMock
    .expectOne(`${service.API_OFFERS}/${culturalOfferId}/comments?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('GET');
    request.error(null);
    tick();

    expect(comments).toBeDefined();
    expect(comments).toBeNull();
  }));

  it('should list some comments', fakeAsync(() => {
    const culturalOfferId = 1;
    const page = 0;
    const mockComments: Comment[] = [
      {
        id: 1,
        user: 'user1',
        culturalOfferId: 1,
        createdAt: 'date1',
        rate: 1,
        text: 'text1',
        images: ['http://localhost:8080/image1']
      },
      {
        id: 2,
        user: 'user2',
        culturalOfferId: 1,
        createdAt: 'date2',
        rate: 2,
        text: 'text2',
        images: ['http://localhost:8080/image2']
      },
      {
        id: 3,
        user: 'user3',
        culturalOfferId: 1,
        createdAt: 'date3',
        rate: 3,
        text: 'text3',
        images: ['http://localhost:8080/image3']
      }
    ];
    let comments: Comment[];

    service.list(culturalOfferId, page).subscribe((res: HttpResponse<Comment[]>) => comments = res.body);
    const request: TestRequest = httpMock
    .expectOne(`${service.API_OFFERS}/${culturalOfferId}/comments?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('GET');
    request.flush(mockComments);
    tick();

    expect(comments).toBeDefined();
    expect(comments.length).toBe(3);

    expect(comments[0].id).toBe(mockComments[0].id);
    expect(comments[0].user).toBe(mockComments[0].user);
    expect(comments[0].culturalOfferId).toBe(mockComments[0].culturalOfferId);
    expect(comments[0].createdAt).toBe(mockComments[0].createdAt);
    expect(comments[0].rate).toBe(mockComments[0].rate);
    expect(comments[0].text).toBe(mockComments[0].text);
    expect(comments[0].images).toBe(mockComments[0].images);

    expect(comments[1].id).toBe(mockComments[1].id);
    expect(comments[1].user).toBe(mockComments[1].user);
    expect(comments[1].culturalOfferId).toBe(mockComments[1].culturalOfferId);
    expect(comments[1].createdAt).toBe(mockComments[1].createdAt);
    expect(comments[1].rate).toBe(mockComments[1].rate);
    expect(comments[1].text).toBe(mockComments[1].text);
    expect(comments[1].images).toBe(mockComments[1].images);

    expect(comments[2].id).toBe(mockComments[2].id);
    expect(comments[2].user).toBe(mockComments[2].user);
    expect(comments[2].culturalOfferId).toBe(mockComments[2].culturalOfferId);
    expect(comments[2].createdAt).toBe(mockComments[2].createdAt);
    expect(comments[2].rate).toBe(mockComments[2].rate);
    expect(comments[2].text).toBe(mockComments[2].text);
    expect(comments[2].images).toBe(mockComments[2].images);

  }));

  it('should emit refreshData', fakeAsync(() => {
    let counter = 0;
    service.refreshData$.subscribe(() =>  ++counter);
    const rateUpdate: RateUpdate = {
      id: 1,
      totalRate: 1
    };

    service.announceRefreshData(rateUpdate);
    tick();
    service.announceRefreshData(rateUpdate);
    tick();
    expect(counter).toBeGreaterThanOrEqual(2);
    expect(service.culturalService.announceUpdateTotalRate).toHaveBeenCalledTimes(2);
    expect(service.culturalService.announceUpdateTotalRate).toHaveBeenCalledWith(rateUpdate);

  }));

  it('should map comment to formData', () => {
    const comment: Comment = {
      id: 1,
      culturalOfferId: 1,
      rate: 1,
      text: 'some text',
    } as Comment;
    const images: Image[] = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];
    const formData: FormData = service.commentToFormData(comment, images);
    expect(formData.get('id')).toEqual(comment.id + '');
    expect(formData.get('culturalOfferId')).toEqual(comment.culturalOfferId + '');
    expect(formData.get('rate')).toEqual(comment.rate + '');
    expect(formData.get('text')).toEqual(comment.text);
    expect(formData.getAll('imagePaths')).toEqual(images.map(image => image.path));
  });

});
