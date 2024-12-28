import { HttpClientTestingModule, HttpTestingController, TestRequest} from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { TypeService } from './type.service';
import { Type } from 'src/app/models/type';
import { Image } from 'src/app/models/image';
import { UniqueCheck } from 'src/app/models/unique-check';
import { SMALL_PAGE_SIZE } from 'src/app/constants/pagination';

describe('TypeService', () => {
  let injector;
  let service: TypeService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    injector = getTestBed();
    service = TestBed.inject(TypeService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save valid type', fakeAsync(() => {
    let type: Type = {
      id: 1,
      name: 'name1',
      category: 'category1'
    } as Type;
    const image: Image = {
      path: 'image1'
    };
    const typeMock: Type = {
      id: 1,
      name: 'name1',
      category: 'category1',
      placemarkIcon: 'image1'
    };

    service.save(type, image).subscribe((res: Type) => type = res);
    const request: TestRequest = httpMock.expectOne(service.API_TYPES);
    expect(request.request.method).toBe('POST');
    request.flush(typeMock);
    tick();

    expect(type).toBeDefined();
    expect(type.id).toBe(typeMock.id);
    expect(type.name).toBe(typeMock.name);
    expect(type.category).toBe(typeMock.category);
    expect(type.placemarkIcon).toBe(typeMock.placemarkIcon);
  }));

  it('should not save invalid type', fakeAsync(() => {
    let type: Type = {
      id: 1,
      name: 'name1'
    } as Type;
    const image: Image = {
      path: 'image1'
    };

    service.save(type, image ).subscribe((res: Type) => type = res);
    const request: TestRequest = httpMock.expectOne(service.API_TYPES);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(type).toBeDefined();
    expect(type).toBeNull();
  }));

  it('should delete valid type', fakeAsync(() => {
    const typeId = 1;
    let response;
    service.delete(typeId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}/${typeId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not delete invalid type', fakeAsync(() => {
    const typeId = 1;
    let response;
    service.delete(typeId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}/${typeId}`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}/has_name`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}/has_name`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}/filter_names`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}/filter_names`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(names).toBeDefined();
    expect(names.length).toBe(0);
  }));

  it('should list some types', fakeAsync(() => {
    const page = 0;
    const typesMock: Type[] = [
      {
        id: 1,
        name: 'name1',
        category: 'category1',
        placemarkIcon: 'placemarkIcon1'
      },
      {
        id: 2,
        name: 'name2',
        category: 'category2',
        placemarkIcon: 'placemarkIcon2'
      },
      {
        id: 3,
        name: 'name3',
        category: 'category3',
        placemarkIcon: 'placemarkIcon3'
      },
    ];
    let types: Type[];

    service.list(page).subscribe((res: HttpResponse<Type[]>) => types = res.body);
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('GET');
    request.flush(typesMock);
    tick();

    expect(types).toBeDefined();
    expect(types.length).toBe(3);

    expect(types[0].id).toBe(typesMock[0].id);
    expect(types[0].name).toBe(typesMock[0].name);
    expect(types[0].category).toBe(typesMock[0].category);
    expect(types[0].placemarkIcon).toBe(typesMock[0].placemarkIcon);

    expect(types[1].id).toBe(typesMock[1].id);
    expect(types[1].name).toBe(typesMock[1].name);
    expect(types[1].category).toBe(typesMock[1].category);
    expect(types[1].placemarkIcon).toBe(typesMock[1].placemarkIcon);

    expect(types[2].id).toBe(typesMock[2].id);
    expect(types[2].name).toBe(typesMock[2].name);
    expect(types[2].category).toBe(typesMock[2].category);
    expect(types[2].placemarkIcon).toBe(typesMock[2].placemarkIcon);
  }));

  it('should list no types', fakeAsync(() => {
    const page = 0;
    let types: Type[];
    service.list(page).subscribe((res: HttpResponse<Type[]>) => types = res as null);
    const request: TestRequest = httpMock.expectOne(`${service.API_TYPES}?page=${page}&size=${SMALL_PAGE_SIZE}`);
    expect(request.request.method).toBe('GET');
    request.error(null);
    tick();

    expect(types).toBeDefined();
    expect(types).toBeNull();
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

  it('should map type to formData', () => {
    const type: Type = {
      id: 1,
      category: 'category1',
      name: 'name1',
    } as Type;
    const image: Image = {
      path: 'image1'
    };
    const formData: FormData = service.typeToFormData(type, image);
    expect(formData.get('category')).toEqual(type.category);
    expect(formData.get('name')).toEqual(type.name);
  });

});
