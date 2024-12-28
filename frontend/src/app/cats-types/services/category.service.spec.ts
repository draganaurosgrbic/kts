import { HttpClientTestingModule, HttpTestingController, TestRequest} from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { CategoryService } from './category.service';
import { Category } from 'src/app/models/category';
import { UniqueCheck } from 'src/app/models/unique-check';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';

describe('CategoryService', () => {
  let injector;
  let service: CategoryService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    injector = getTestBed();
    service = TestBed.inject(CategoryService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save valid category', fakeAsync(() => {
    let category: Category = {
      id: 1,
      name: 'name1'
    };
    const categoryMock: Category = {
      id: 1,
      name: 'name1'
    };

    service.save(category).subscribe((res: Category) => category = res);
    const request: TestRequest = httpMock.expectOne(service.API_CATEGORIES);
    expect(request.request.method).toBe('POST');
    request.flush(categoryMock);
    tick();

    expect(category).toBeDefined();
    expect(category.id).toBe(categoryMock.id);
    expect(category.name).toBe(categoryMock.name);
  }));

  it('should not save invalid category', fakeAsync(() => {
    let category: Category = {
      id: 1,
      name: 'name1'
    };

    service.save(category).subscribe((res: Category) => category = res);
    const request: TestRequest = httpMock.expectOne(service.API_CATEGORIES);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(category).toBeDefined();
    expect(category).toBeNull();
  }));

  it('should delete valid category', fakeAsync(() => {
    const categoryId = 1;
    let response;
    service.delete(categoryId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}/${categoryId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not delete invalid category', fakeAsync(() => {
    const categoryId = 1;
    let response;
    service.delete(categoryId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}/${categoryId}`);
    expect(request.request.method).toBe('DELETE');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));

  it('should recognize taken name', fakeAsync(() => {
    const param: UniqueCheck = {
      id: 1,
      name: 'name1'
    };
    let response: boolean;

    service.hasName(param).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}/has_name`);
    expect(request.request.method).toBe('POST');
    request.flush({value: true});
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should recognize free name', fakeAsync(() => {
    const param: UniqueCheck = {
      id: 1,
      name: 'name1'
    };
    let response: boolean;

    service.hasName(param).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}/has_name`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));

  it('should filter some names', fakeAsync(() => {
    const namesMock: string[] = ['name1', 'name2', 'name3'];
    let names: string[];

    service.filterNames('filter').subscribe((response: string[]) => names = response);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}/filter_names`);
    expect(request.request.method).toBe('POST');
    request.flush(namesMock);
    tick();

    expect(names).toBeDefined();
    expect(names.length).toBe(3);
    expect(names[0]).toBe(namesMock[0]);
    expect(names[1]).toBe(namesMock[1]);
    expect(names[2]).toBe(namesMock[2]);
  }));

  it('should filter no names', fakeAsync(() => {
    let names: string[];
    service.filterNames('filter').subscribe((response: string[]) => names = response);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}/filter_names`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(names).toBeDefined();
    expect(names.length).toBe(0);
  }));

  it('should list some categories', fakeAsync(() => {
    const page = 0;
    const categoriesMock: Category[] = [
      {
        id: 1,
        name: 'name1'
      },
      {
        id: 2,
        name: 'name2'
      },
      {
        id: 3,
        name: 'name3'
      },
    ];
    let categories: Category[];

    service.list(page).subscribe((res: HttpResponse<Category[]>) => categories = res.body);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('GET');
    request.flush(categoriesMock);
    tick();

    expect(categories).toBeDefined();
    expect(categories.length).toBe(3);

    expect(categories[0].id).toBe(categoriesMock[0].id);
    expect(categories[0].name).toBe(categoriesMock[0].name);

    expect(categories[1].id).toBe(categoriesMock[1].id);
    expect(categories[1].name).toBe(categoriesMock[1].name);

    expect(categories[2].id).toBe(categoriesMock[2].id);
    expect(categories[2].name).toBe(categoriesMock[2].name);
  }));

  it('should list no categories', fakeAsync(() => {
    const page = 0;
    let categories: Category[];
    service.list(page).subscribe((res: HttpResponse<Category[]>) => categories = res as null);
    const request: TestRequest = httpMock.expectOne(`${service.API_CATEGORIES}?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('GET');
    request.error(null);
    tick();

    expect(categories).toBeDefined();
    expect(categories).toBeNull();
  }));

  it('should emit refreshData', fakeAsync(() => {
    let counter = 0;
    service.refreshData$.subscribe(() =>  ++counter);

    service.announceRefreshData();
    tick();
    service.announceRefreshData();
    tick();
    expect(counter).toBe(2);
  }));
});
