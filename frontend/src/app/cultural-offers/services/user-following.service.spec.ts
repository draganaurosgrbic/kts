import { HttpClient, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import { LARGE_PAGE_SIZE } from 'src/app/constants/pagination';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { FilterParams } from 'src/app/models/filter-params';

import { UserFollowingService } from './user-following.service';

describe('UserFollowingService', () => {
  let injector;
  let service: UserFollowingService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    injector = getTestBed();
    service = TestBed.inject(UserFollowingService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

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
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_followings?page=${page}&size=${LARGE_PAGE_SIZE}`);
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
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/filter_followings?page=${page}&size=${LARGE_PAGE_SIZE}`);
    expect(request.request.method).toBe('POST');
    request.error(null);
    tick();

    expect(offers).toBeDefined();
    expect(offers).toBeNull();
  }));

  it('should toggle valid subscription', fakeAsync(() => {
    const offerId = 1;
    let response;
    service.toggleSubscription(offerId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/${offerId}/toggle_subscription`);
    expect(request.request.method).toBe('GET');
    request.flush(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeTrue();
  }));

  it('should not toggle invalid subscription', fakeAsync(() => {
    const offerId = 1;
    let response;
    service.toggleSubscription(offerId).subscribe((res: boolean) => response = res);
    const request: TestRequest = httpMock.expectOne(`${service.API_OFFERS}/${offerId}/toggle_subscription`);
    expect(request.request.method).toBe('GET');
    request.error(null);
    tick();

    expect(response).toBeDefined();
    expect(response).toBeFalse();
  }));

});
