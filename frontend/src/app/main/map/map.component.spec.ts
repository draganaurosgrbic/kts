import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { MapComponent } from './map.component';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { MatDialog } from '@angular/material/dialog';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { of } from 'rxjs';
import { CulturalDialogComponent } from 'src/app/cultural-offers/cultural-dialog/cultural-dialog.component';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { By } from '@angular/platform-browser';

describe('MapComponent', () => {
  let component: MapComponent;
  let fixture: ComponentFixture<MapComponent>;
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
      image: 'image2',
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
      image: 'image3',
      totalRate: 3,
      followed: true
    }
  ];

  beforeEach(async () => {
    const culturalServiceMock = {};
    const dialogMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ MapComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: CulturalService, useValue: culturalServiceMock},
        {provide: MatDialog, useValue: dialogMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MapComponent);
    component = fixture.componentInstance;
    component.mapPending = false;
    component.fetchPending = false;
    component.culturalService.markOnMap$ = of(offerMock);
    component.ymaps = {
      instance: {
        balloon: {
          open: jasmine.createSpy('open')
        }
      }
    };
    spyOn(component, 'markOnMap').and.callThrough();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render preloader when pending', () => {
    component.mapPending = true;
    component.fetchPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-preloader'))).toBeTruthy();
    component.mapPending = true;
    component.fetchPending = false;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-preloader'))).toBeTruthy();
    component.mapPending = false;
    component.fetchPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-preloader'))).toBeTruthy();
  });

  it('should render no placemarks', () => {
    component.culturalOffers = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('ya-placemark'))).toBeFalsy();
  });

  it('should render some placemarks', () => {
    component.culturalOffers = offersMock;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('ya-placemark'));
    expect(de.length).toEqual(3);
    expect(de[0].nativeElement.geometry).toEqual([1, 1]);
    expect(de[1].nativeElement.geometry).toEqual([2, 2]);
    expect(de[2].nativeElement.geometry).toEqual([3, 3]);
  });

  it('should subscribe to markOnMap$', fakeAsync(() => {
    tick();
    expect(component.markOnMap).toHaveBeenCalledTimes(1);
    expect(component.markOnMap).toHaveBeenCalledWith(offerMock);
    expect(component.ymaps.instance.balloon.open).toHaveBeenCalledTimes(1);
    expect(component.ymaps.instance.balloon.open).toHaveBeenCalledWith(
      component.mapCenter,
      `<div style='text-align: center; font-weight: bold;'>${offerMock.name} is placed here!</div>`);
  }));

  it('should open details dialog', () => {
    component.showDetails(offerMock);
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CulturalDialogComponent,
      {...DIALOG_OPTIONS, ...{data: offerMock}});
  });

  it('should return map center', () => {
    component.center = null;
    expect(component.mapCenter).toEqual(component.DEFAULT_CENTER);
    component.center = offerMock;
    expect(component.mapCenter).toEqual([offerMock.lat, offerMock.lng]);
  });

  it('should return placemark options', () => {
    const response: any = component.placemarkOptions(offerMock);
    expect(response).toEqual({
      iconLayout: 'default#image',
      iconImageHref: offerMock.placemarkIcon
    });
  });

  it('should return placemark properties', () => {
    const response: any = component.placemarkProperties(offerMock);
    expect(response).toEqual({
      hintContent: `
        <div style="text-align: center;">
          <div>
            <img src='${offerMock.image}' height="100" width="100" alt='no image'>
          </div>
          <div style="font-weight: bold">
          ${offerMock.name}
          </div>
          <div>
            ${offerMock.location}
          </div>
        </div>
      `
    });
  });

});
