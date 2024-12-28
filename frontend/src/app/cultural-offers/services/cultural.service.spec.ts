import { HttpClient, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { Image } from 'src/app/models/image';
import { FilterParams } from 'src/app/models/filter-params';

import { CulturalService } from './cultural.service';
import { LARGE_PAGE_SIZE } from 'src/app/constants/pagination';
import { UniqueCheck } from 'src/app/models/unique-check';

describe('CulturalService', () => {
  let injector;
  let service: CulturalService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    injector = getTestBed();
    service = TestBed.inject(CulturalService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save valid offer', fakeAsync(() => {
    let offer: CulturalOffer = {
      id: 1,
      type: 'type1',
      name: 'name1',
      location: 'location1',
      lat: 1,
      lng: 1,
      description: 'description1',
    } as CulturalOffer;
    const image: Image = {
      path: 'image1'
    };
    const offerMock: CulturalOffer = {
      id: 1,
      category: 'category1',
      type: 'type1',
      placemarkIcon: 'placemark1',
      name: 'name1',
      location: 'location1',
      lat: 1,
      lng: 1,
      description: 'description1',
      image: 'image1',
      followed: true,
      totalRate: 1
    };

    service.save(offer, image).subscribe((res: CulturalOffer) => offer = res);
    const request: TestRequest = httpMock.expectOne(service.API_OFFERS);
    expect(request.request.method).toBe('POST');
    request.flush(offerMock);
    tick();

    expect(offer).toBeDefined();
    expect(offer.id).toBe(offerMock.id);
    expect(offer.category).toBe(offerMock.category);
    expect(offer.type).toBe(offerMock.type);
    expect(offer.placemarkIcon).toBe(offerMock.placemarkIcon);
    expect(offer.name).toBe(offerMock.name);
    expect(offer.location).toBe(offerMock.location);
    expect(offer.lat).toBe(offerMock.lat);
    expect(offer.lng).toBe(offerMock.lng);
    expect(offer.description).toBe(offerMock.description);
    expect(offer.image).toBe(offerMock.image);
    expect(offer.followed).toBe(offerMock.followed);
    expect(offer.totalRate).toBe(offerMock.totalRate);
  }));

  it('should not save invalid offer', fakeAsync(() => {
    let offer: CulturalOffer = {
      id: 1,
      type: 'type1',
      name: 'name1',
      location: 'location1',
      lat: 1,
      lng: 1,
      description: 'description1',
    } as CulturalOffer;
    const image: Image = {
      path: 'image1'
    };

    service.save(offer, image).subscribe((res: CulturalOffer) => offer = res);
    const request: TestRequest = httpMock.expectOne(service.API_OFFERS);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(offer).toBeDefined();
    expect(offer).toBeNull();
  }));

  it('should delete valid offer', fakeAsync(() => {
    const offerId = 1;
    let response;
    service.delete(offerId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/${offerId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not delete invalid offer', fakeAsync(() => {
    const offerId = 1;
    let response;
    service.delete(offerId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/${offerId}`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/has_name`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/has_name`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_names`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_names`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(names).toBeDefined();
    expect(names.length).toBe(0);
  }));

  it('should filter some locations', fakeAsync(() => {
    const locationsMock: string[] = ['location1', 'location2', 'location3'];
    let locations: string[];

    service.filterLocations('filter').subscribe((response: string[]) => locations = response);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_locations`);
    expect(request.request.method).toBe('POST');
    request.flush(locationsMock);
    tick();

    expect(locations).toBeDefined();
    expect(locations.length).toBe(3);
    expect(locations[0]).toBe(locationsMock[0]);
    expect(locations[1]).toBe(locationsMock[1]);
    expect(locations[2]).toBe(locationsMock[2]);
  }));

  it('should filter no locations', fakeAsync(() => {
    let locations: string[];
    service.filterLocations('filter').subscribe((response: string[]) => locations = response);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_locations`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(locations).toBeDefined();
    expect(locations.length).toBe(0);
  }));

  it('should filter some types', fakeAsync(() => {
    const typesMock: string[] = ['type1', 'type2', 'type3'];
    let types: string[];

    service.filterTypes('filter').subscribe((response: string[]) => types = response);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_types`);
    expect(request.request.method).toBe('POST');
    request.flush(typesMock);
    tick();

    expect(types).toBeDefined();
    expect(types.length).toBe(3);
    expect(types[0]).toBe(typesMock[0]);
    expect(types[1]).toBe(typesMock[1]);
    expect(types[2]).toBe(typesMock[2]);
  }));

  it('should filter no types', fakeAsync(() => {
    let types: string[];
    service.filterTypes('filter').subscribe((response: string[]) => types = response);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_types`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(types).toBeDefined();
    expect(types.length).toBe(0);
  }));

  it('should filter some offers', fakeAsync(() => {
    const filters: FilterParams = {
      name: 'name1',
      location: 'location1',
      type: 'type1'
    };
    const page = 0;
    const offersMock: CulturalOffer[] = [
      {
        id: 1,
        category: 'category1',
        type: 'type1',
        placemarkIcon: 'placemark1',
        name: 'name1',
        location: 'location1',
        lat: 1,
        lng: 1,
        description: 'description1',
        image: 'image1',
        followed: true,
        totalRate: 1
      },
      {
        id: 2,
        category: 'category2',
        type: 'type2',
        placemarkIcon: 'placemark2',
        name: 'name2',
        location: 'location2',
        lat: 2,
        lng: 2,
        description: 'description2',
        image: 'image2',
        followed: false,
        totalRate: 2
      },
      {
        id: 3,
        category: 'category3',
        type: 'type3',
        placemarkIcon: 'placemark3',
        name: 'name3',
        location: 'location3',
        lat: 3,
        lng: 3,
        description: 'description3',
        image: 'image3',
        followed: true,
        totalRate: 3
      },
    ];
    let offers: CulturalOffer[];

    service.filter(filters, page).subscribe((res: HttpResponse<CulturalOffer[]>) => offers = res.body);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter?page=${page}&size=${LARGE_PAGE_SIZE}`);
    expect(request.request.method).toBe('POST');
    request.flush(offersMock);
    tick();

    expect(offers).toBeDefined();
    expect(offers.length).toBe(3);

    expect(offers[0].id).toBe(offersMock[0].id);
    expect(offers[0].category).toBe(offersMock[0].category);
    expect(offers[0].type).toBe(offersMock[0].type);
    expect(offers[0].placemarkIcon).toBe(offersMock[0].placemarkIcon);
    expect(offers[0].name).toBe(offersMock[0].name);
    expect(offers[0].location).toBe(offersMock[0].location);
    expect(offers[0].lat).toBe(offersMock[0].lat);
    expect(offers[0].lng).toBe(offersMock[0].lng);
    expect(offers[0].description).toBe(offersMock[0].description);
    expect(offers[0].image).toBe(offersMock[0].image);
    expect(offers[0].followed).toBe(offersMock[0].followed);
    expect(offers[0].totalRate).toBe(offersMock[0].totalRate);

    expect(offers[1].id).toBe(offersMock[1].id);
    expect(offers[1].category).toBe(offersMock[1].category);
    expect(offers[1].type).toBe(offersMock[1].type);
    expect(offers[1].placemarkIcon).toBe(offersMock[1].placemarkIcon);
    expect(offers[1].name).toBe(offersMock[1].name);
    expect(offers[1].location).toBe(offersMock[1].location);
    expect(offers[1].lat).toBe(offersMock[1].lat);
    expect(offers[1].lng).toBe(offersMock[1].lng);
    expect(offers[1].description).toBe(offersMock[1].description);
    expect(offers[1].image).toBe(offersMock[1].image);
    expect(offers[1].followed).toBe(offersMock[1].followed);
    expect(offers[1].totalRate).toBe(offersMock[1].totalRate);

    expect(offers[2].id).toBe(offersMock[2].id);
    expect(offers[2].category).toBe(offersMock[2].category);
    expect(offers[2].type).toBe(offersMock[2].type);
    expect(offers[2].placemarkIcon).toBe(offersMock[2].placemarkIcon);
    expect(offers[2].name).toBe(offersMock[2].name);
    expect(offers[2].location).toBe(offersMock[2].location);
    expect(offers[2].lat).toBe(offersMock[2].lat);
    expect(offers[2].lng).toBe(offersMock[2].lng);
    expect(offers[2].description).toBe(offersMock[2].description);
    expect(offers[2].image).toBe(offersMock[2].image);
    expect(offers[2].followed).toBe(offersMock[2].followed);
    expect(offers[2].totalRate).toBe(offersMock[2].totalRate);
  }));

  it('should filter no offers', fakeAsync(() => {
    const filters: FilterParams = {
      name: 'name1',
      location: 'location1',
      type: 'type1'
    };
    const page = 0;
    let offers: CulturalOffer[];
    service.filter(filters, page).subscribe((res: HttpResponse<CulturalOffer[]>) => offers = res as null);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter?page=${page}&size=${LARGE_PAGE_SIZE}`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(offers).toBeDefined();
    expect(offers).toBeNull();
  }));

  it('should emit refreshData', fakeAsync(() => {
    let counter = 0;
    service.refreshData$.subscribe(() =>  ++counter);

    service.announceRefreshData(null);
    tick();
    service.announceRefreshData(null);
    tick();
    expect(counter).toBe(2);
  }));

  it('should emit filterData', fakeAsync(() => {
    let counter = 0;
    service.filterData$.subscribe(() =>  ++counter);

    service.announceFilterData(null);
    tick();
    service.announceFilterData(null);
    tick();
    expect(counter).toBe(2);
  }));

  it('should emit markOnMap', fakeAsync(() => {
    let counter = 0;
    service.markOnMap$.subscribe(() =>  ++counter);

    service.announceMarkOnMap(null);
    tick();
    service.announceMarkOnMap(null);
    tick();
    expect(counter).toBe(2);
  }));

  it('should emit updateTotalRate', fakeAsync(() => {
    let counter = 0;
    service.updateTotalRate$.subscribe(() =>  ++counter);

    service.announceUpdateTotalRate(null);
    tick();
    service.announceUpdateTotalRate(null);
    tick();
    expect(counter).toBe(2);
  }));

  it('should map offer to formData', () => {
    const offer: CulturalOffer = {
      id: 1,
      type: 'type1',
      name: 'name1',
      location: 'location1',
      lat: 1,
      lng: 1,
      description: 'description1',
    } as CulturalOffer;
    const image: Image = {
      path: 'image1'
    };
    const formData: FormData = service.offerToFormData(offer, image);
    expect(formData.get('id')).toEqual(offer.id + '');
    expect(formData.get('type')).toEqual(offer.type);
    expect(formData.get('name')).toEqual(offer.name);
    expect(formData.get('location')).toEqual(offer.location);
    expect(formData.get('lat')).toEqual(offer.lat + '');
    expect(formData.get('lng')).toEqual(offer.lng + '');
    expect(formData.get('description')).toEqual(offer.description);
    expect(formData.get('imagePath')).toEqual(image.path);
  });

});
