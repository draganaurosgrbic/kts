import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { ADMIN_ROLE, GUEST_ROLE } from 'src/app/constants/roles';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { UserFollowingService } from 'src/app/cultural-offers/services/user-following.service';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: 'admin',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1'
  };
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
      image: 'http://localhost:8080/image1',
      totalRate: 1,
      followed: true
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
      image: 'http://localhost:8080/image2',
      totalRate: 2,
      followed: true
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
      image: 'http://localhost:8080/image3',
      totalRate: 3,
      followed: true
    }
  ];
  const offersMockFirstUnfollowed: CulturalOffer[] = [
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
      image: 'http://localhost:8080/image1',
      totalRate: 1,
      followed: false
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
      image: 'http://localhost:8080/image2',
      totalRate: 2,
      followed: true
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
      image: 'http://localhost:8080/image3',
      totalRate: 3,
      followed: true
    }
  ];

  const offersMockFourthAdded: CulturalOffer[] = [
    {
      id: 4,
      category: 'category4',
      type: 'type4',
      placemarkIcon: 'placemark4',
      name: 'name4',
      location: 'location4',
      lat: 4,
      lng: 4,
      description: 'description4',
      image: 'http://localhost:8080/image4',
      totalRate: 4,
      followed: true
    },
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
      image: 'http://localhost:8080/image1',
      totalRate: 1,
      followed: true
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
      image: 'http://localhost:8080/image2',
      totalRate: 2,
      followed: true
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
      image: 'http://localhost:8080/image3',
      totalRate: 3,
      followed: true
    }
  ];

  const offersMockSecondDeleted: CulturalOffer[] = [
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
      image: 'http://localhost:8080/image1',
      totalRate: 1,
      followed: true
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
      image: 'http://localhost:8080/image3',
      totalRate: 3,
      followed: true
    }
  ];

  beforeEach(async () => {
    const fetchResponse = {
      body: offersMock.filter(() => true),
      headers: {
        get: jasmine.createSpy('get').and.returnValue('false')
      }
    };
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock)
    };
    const culturalServiceMock = {
      filter: jasmine.createSpy('filter').and.returnValue(of(fetchResponse))
    };
    const userFollowingServiceMock = {
      filter: jasmine.createSpy('filter').and.returnValue(of(fetchResponse))
    };
    await TestBed.configureTestingModule({
      declarations: [ HomeComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: CulturalService, useValue: culturalServiceMock},
        {provide: UserFollowingService, useValue: userFollowingServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    spyOn(component, 'fetchData').and.callThrough();
    spyOn(component, 'changePage').and.callThrough();
    spyOn(component, 'refreshData').and.callThrough();
    component.culturalService.refreshData$ = of(0);
    component.culturalService.filterData$ = of({
      name: '',
      location: '',
      type: ''
    });
    component.selectedTab = 0;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should subscribe to filterData$ and refreshData$', fakeAsync(() => {
    tick();
    expect(component.changePage).toHaveBeenCalledTimes(2);
    expect(component.changePage).toHaveBeenCalledWith(0);
    expect(component.fetchData).toHaveBeenCalledTimes(2);
    expect(component.fetchData).toHaveBeenCalledWith();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);
    expect(component.culturalService.filter).toHaveBeenCalledTimes(2);
    expect(component.culturalService.filter).toHaveBeenCalledWith(
      component.filterParams[component.selectedTab], component.pagination[component.selectedTab].pageNumber);
    expect(component.culturalOffers[component.selectedTab]).toEqual(offersMock);
    expect(component.pagination[component.selectedTab].firstPage).toBeFalse();
    expect(component.pagination[component.selectedTab].lastPage).toBeFalse();
  }));

  it('should render tabs when current user is guest', () => {
    userMock.role = GUEST_ROLE;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.tabs'))).toBeTruthy();
  });

  it('should not render tabs when current user is admin', () => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.tabs'))).toBeFalsy();
  });

  it('should pass offers to map and list component', () => {
    expect(fixture.debugElement.query(By.css('app-map')).nativeElement.culturalOffers)
    .toEqual(component.culturalOffers[component.selectedTab]);
    expect(fixture.debugElement.query(By.css('app-cultural-list')).nativeElement.culturalOffers)
    .toEqual(component.culturalOffers[component.selectedTab]);
  });

  it('should pass followings to map and list component', () => {
    component.selectedTab = 1;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-map')).nativeElement.culturalOffers)
    .toEqual(component.culturalOffers[component.selectedTab]);
    expect(fixture.debugElement.query(By.css('app-cultural-list')).nativeElement.culturalOffers)
    .toEqual(component.culturalOffers[component.selectedTab]);
  });

  it('should change page', fakeAsync(() => {
    const page = 3;
    fixture.debugElement.query(By.css('app-cultural-list')).triggerEventHandler('changedPage', page);
    tick();
    expect(component.changePage).toHaveBeenCalledWith(page);
    expect(component.fetchData).toHaveBeenCalledWith();
    expect(component.culturalService.filter).toHaveBeenCalledWith(
      component.filterParams[component.selectedTab], page);
  }));

  it('should change tab', fakeAsync(() => {
    component.changeTab(1);
    tick();
    expect(component.changePage).toHaveBeenCalledWith(0);
    expect(component.fetchData).toHaveBeenCalledWith();
    expect(component.userFollowingService.filter).toHaveBeenCalledWith(
      component.filterParams[component.selectedTab], 0);
  }));

  it('should refresh data by removing the second offer', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(2);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(2);
    expect(component.culturalOffers[0]).toEqual(offersMockSecondDeleted);
  }));

  it('should not refresh data by removing an offer with invalid id', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(-1);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(-1);
    expect(component.culturalOffers[0]).toEqual(offersMock);
  }));

  it('should not refresh data by removing an offer when guest', fakeAsync(() => {
    userMock.role = GUEST_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(2);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(2);
    expect(component.culturalOffers[0]).toEqual(offersMock);
  }));

  it('should not refresh data by removing an offer with invalid id when guest', fakeAsync(() => {
    userMock.role = GUEST_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(-1);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(-1);
    expect(component.culturalOffers[0]).toEqual(offersMock);
  }));

  it('should refresh data by adding an offer', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(offersMockFourthAdded[0]);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(offersMockFourthAdded[0]);
    expect(component.culturalOffers[0]).toEqual(offersMockFourthAdded);
  }));

  it('should not refresh data by adding an offer when guest', fakeAsync(() => {
    userMock.role = GUEST_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(offersMockFourthAdded[0]);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(offersMockFourthAdded[0]);
    expect(component.culturalOffers[0]).toEqual(offersMock);
  }));

  it('should refresh data by updating an offer', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(offersMock[1]);

    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(offersMock[1]);
    expect(component.culturalOffers[0]).toEqual(offersMock);
  }));

  it('should refresh data by adding an offer to the user-following list', fakeAsync(() => {
    userMock.role = GUEST_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(offersMock[0]);
    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(offersMock[0]);
    expect(component.culturalOffers[0]).toEqual(offersMock);
    expect(component.culturalOffers[1]).toEqual([offersMock[0]]);
  }));

  it('should not refresh data by altering the user-following list when admin', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    tick();
    expect(component.refreshData).toHaveBeenCalledTimes(1);
    expect(component.refreshData).toHaveBeenCalledWith(0);

    component.refreshData(offersMock[0]);
    expect(component.refreshData).toHaveBeenCalledTimes(2);
    expect(component.refreshData).toHaveBeenCalledWith(offersMock[0]);
    expect(component.culturalOffers[0]).toEqual(offersMock);
    expect(component.culturalOffers[1].length).toEqual(0);
  }));

});
