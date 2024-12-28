import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { CulturalListComponent } from './cultural-list.component';

describe('CulturalListComponent', () => {
  let component: CulturalListComponent;
  let fixture: ComponentFixture<CulturalListComponent>;
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
    await TestBed.configureTestingModule({
      declarations: [ CulturalListComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalListComponent);
    component = fixture.componentInstance;
    component.fetchPending = false;
    spyOn(component.changedPage, 'emit');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render preloader when pending', () => {
    component.fetchPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-preloader'))).toBeTruthy();
  });

  it('should render no offers', () => {
    component.culturalOffers = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-cultural-details'))).toBeFalsy();
  });

  it('should render some offers', () => {
    component.culturalOffers = offersMock;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-cultural-details'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.culturalOffer).toEqual(component.culturalOffers[0]);
    expect(de[1].nativeElement.culturalOffer).toEqual(component.culturalOffers[1]);
    expect(de[2].nativeElement.culturalOffer).toEqual(component.culturalOffers[2]);
  });

  it('should emit changed page', () => {
    const page = 3;
    fixture.debugElement.query(By.css('app-paginator')).triggerEventHandler('changedPage', page);
    expect(component.changedPage.emit).toHaveBeenCalledTimes(1);
    expect(component.changedPage.emit).toHaveBeenCalledWith(page);
  });

});
