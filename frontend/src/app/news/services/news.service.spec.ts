import { HttpClient, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';
import { News } from 'src/app/models/news';
import { Image } from 'src/app/models/image';
import { NewsService } from './news.service';

describe('NewsService', () => {
  let injector;
  let service: NewsService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  const dateStart: Date = new Date(2021, 1, 1);
  const dateEnd: Date = new Date(2021, 1, 2);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    injector = getTestBed();
    service = TestBed.inject(NewsService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save valid news', fakeAsync(() => {
    let news: News = {
      id: 1,
      culturalOfferId: 1,
      text: 'some text',
    } as News;
    const images: Image[] = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];
    const newsMock: News = {
      id: 1,
      culturalOfferId: 1,
      createdAt: new Date(),
      text: 'some text',
      images: ['http://localhost:8080/image1',
               'http://localhost:8080/image2',
               'http://localhost:8080/image3']
    };

    service.save(news, images).subscribe((res: News) => news = res);
    const request: TestRequest = httpMock.expectOne(service.API_NEWS);
    expect(request.request.method).toBe('POST');
    request.flush(newsMock);
    tick();

    expect(news).toBeDefined();
    expect(news.id).toBe(newsMock.id);
    expect(news.culturalOfferId).toBe(newsMock.culturalOfferId);
    expect(news.createdAt).toBe(newsMock.createdAt);
    expect(news.text).toBe(newsMock.text);
    expect(news.images).toBe(newsMock.images);
  }));

  it('should not save invalid news', fakeAsync(() => {
    let news: News = {
      id: 1,
      culturalOfferId: 1,
      text: 'some text',
    } as News;
    const images: Image[] = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];

    service.save(news, images).subscribe((res: News) => news = res);
    const request: TestRequest = httpMock.expectOne(service.API_NEWS);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(news).toBeDefined();
    expect(news).toBeNull();
  }));

  it('should delete valid news', fakeAsync(() => {
    const newsId = 1;
    let response;
    service.delete(newsId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_NEWS}/${newsId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not delete invalid news', fakeAsync(() => {
    const newsId = 1;
    let response;
    service.delete(newsId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_NEWS}/${newsId}`);
    expect(request.request.method).toBe('DELETE');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));

  it('should list no news', fakeAsync(() => {
    const culturalOfferId = 1;
    const page = 0;
    let news: News[];

    service.filter({startDate: null, endDate: null}, culturalOfferId, page).subscribe((res: HttpResponse<News[]>) => news = res as null);
    const request: TestRequest = httpMock
    .expectOne(`${service.API_OFFERS}/${culturalOfferId}/filter_news?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(news).toBeDefined();
    expect(news).toBeNull();
  }));

  it('should list some news', fakeAsync(() => {
    const culturalOfferId = 1;
    const page = 0;
    const mockNews: News[] = [
      {
        id: 1,
        culturalOfferId,
        createdAt: new Date(2021, 1, 1),
        text: 'text1',
        images: ['http://localhost:8080/image1',
        'http://localhost:8080/image2',
        'http://localhost:8080/image3']
        },
      {
        id: 2,
        culturalOfferId,
        createdAt: new Date(2021, 1, 2),
        text: 'text2',
        images: ['http://localhost:8080/image1',
        'http://localhost:8080/image2',
        'http://localhost:8080/image3']
        },
      {
        id: 3,
        culturalOfferId,
        createdAt: new Date(2021, 1, 3),
        text: 'text3',
        images: ['http://localhost:8080/image1',
        'http://localhost:8080/image2',
        'http://localhost:8080/image3']
      }
    ];
    let news: News[];

    service.filter({startDate: null, endDate: null}, culturalOfferId, page).subscribe((res: HttpResponse<News[]>) => news = res.body);
    const request: TestRequest = httpMock
    .expectOne(`${service.API_OFFERS}/${culturalOfferId}/filter_news?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('POST');
    request.flush(mockNews);
    tick();

    expect(news).toBeDefined();
    expect(news.length).toBe(3);

    expect(news[0].id).toBe(mockNews[0].id);
    expect(news[0].culturalOfferId).toBe(mockNews[0].culturalOfferId);
    expect(news[0].createdAt).toBe(mockNews[0].createdAt);
    expect(news[0].text).toBe(mockNews[0].text);
    expect(news[0].images).toBe(mockNews[0].images);

    expect(news[1].id).toBe(mockNews[1].id);
    expect(news[1].culturalOfferId).toBe(mockNews[1].culturalOfferId);
    expect(news[1].createdAt).toBe(mockNews[1].createdAt);
    expect(news[1].text).toBe(mockNews[1].text);
    expect(news[1].images).toBe(mockNews[1].images);

    expect(news[2].id).toBe(mockNews[2].id);
    expect(news[2].culturalOfferId).toBe(mockNews[2].culturalOfferId);
    expect(news[2].createdAt).toBe(mockNews[2].createdAt);
    expect(news[2].text).toBe(mockNews[2].text);
    expect(news[2].images).toBe(mockNews[2].images);

  }));

  it('should filter some news', fakeAsync(() => {
    const culturalOfferId = 1;
    const page = 0;
    const mockNews: News[] = [
      {
        id: 1,
        culturalOfferId,
        createdAt: new Date(2021, 1, 1),
        text: 'text1',
        images: ['http://localhost:8080/image1',
        'http://localhost:8080/image2',
        'http://localhost:8080/image3']
        },
      {
        id: 2,
        culturalOfferId,
        createdAt: new Date(2021, 1, 2),
        text: 'text2',
        images: ['http://localhost:8080/image1',
        'http://localhost:8080/image2',
        'http://localhost:8080/image3']
        }
    ];
    let news: News[];

    service.filter({startDate: dateStart, endDate: dateEnd}, culturalOfferId, page)
    .subscribe((res: HttpResponse<News[]>) => news = res.body);
    const request: TestRequest = httpMock
    .expectOne(`${service.API_OFFERS}/${culturalOfferId}/filter_news?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('POST');
    request.flush(mockNews);
    tick();

    expect(news).toBeDefined();
    expect(news.length).toBe(2);

    expect(news[0].id).toBe(mockNews[0].id);
    expect(news[0].culturalOfferId).toBe(mockNews[0].culturalOfferId);
    expect(news[0].createdAt).toBe(mockNews[0].createdAt);
    expect(news[0].text).toBe(mockNews[0].text);
    expect(news[0].images).toBe(mockNews[0].images);

    expect(news[1].id).toBe(mockNews[1].id);
    expect(news[1].culturalOfferId).toBe(mockNews[1].culturalOfferId);
    expect(news[1].createdAt).toBe(mockNews[1].createdAt);
    expect(news[1].text).toBe(mockNews[1].text);
    expect(news[1].images).toBe(mockNews[1].images);

  }));

  it('should emit refreshData', fakeAsync(() => {
    let counter = 0;
    service.refreshData$.subscribe(() =>  ++counter);
    const culturalOfferId = 1;

    service.announceRefreshData(culturalOfferId);
    tick();
    service.announceRefreshData(culturalOfferId);
    tick();
    expect(counter).toBeGreaterThanOrEqual(2);
  }));

  it('should map news to formData', () => {
    const news: News = {
      id: 1,
      culturalOfferId: 1,
      text: 'some text',
    } as News;
    const images: Image[] = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];

    const formData: FormData = service.newsToFormData(news, images);
    expect(formData.get('id')).toEqual(news.id + '');
    expect(formData.get('culturalOfferId')).toEqual(news.culturalOfferId + '');
    expect(formData.get('text')).toEqual(news.text);
    expect(formData.getAll('imagePaths')).toEqual(images.map(image => image.path));
  });

});
