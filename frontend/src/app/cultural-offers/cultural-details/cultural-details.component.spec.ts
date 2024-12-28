import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { CulturalDialogComponent } from '../cultural-dialog/cultural-dialog.component';
import { CulturalService } from '../services/cultural.service';
import { CulturalDetailsComponent } from './cultural-details.component';

describe('CulturalDetailsComponent', () => {
  let component: CulturalDetailsComponent;
  let fixture: ComponentFixture<CulturalDetailsComponent>;
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
    image: 'http://localhost:8080/image1',
    followed: true,
    totalRate: 1
  };

  beforeEach(async () => {
    const culturalServiceMock = {
      announceMarkOnMap: jasmine.createSpy('announceMarkOnMap')
    };
    const dialogMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ CulturalDetailsComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: CulturalService, useValue: culturalServiceMock},
        {provide: MatDialog, useValue: dialogMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalDetailsComponent);
    component = fixture.componentInstance;
    component.culturalOffer = offerMock;
    component.culturalService.updateTotalRate$ = of({id: offerMock.id, totalRate: 0});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should subscribe to updateTotalRate$', fakeAsync(() => {
    tick();
    expect(component.culturalOffer.totalRate).toEqual(0);
  }));

  it('should render cultural details', () => {
    expect(fixture.debugElement.query(By.css('app-bold-text')).nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.name);
    expect(fixture.debugElement.queryAll(By.css('span'))[0].nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.type + ', ' + component.culturalOffer.category);
    expect(fixture.debugElement.queryAll(By.css('span'))[1].nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.location);
    expect(fixture.debugElement.queryAll(By.css('span'))[2].nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.description);
    expect(fixture.debugElement.queryAll(By.css('span'))[3].nativeElement.textContent)
    .toEqual(component.culturalOffer.totalRate + '.00/5');
    expect(fixture.debugElement.query(By.css('img')).nativeElement.src)
    .toEqual(component.culturalOffer.image);
  });

  it('should emit markOnMap event when clicked', () => {
    fixture.debugElement.query(By.css('div')).triggerEventHandler('click', null);
    expect(component.culturalService.announceMarkOnMap).toHaveBeenCalledTimes(1);
    expect(component.culturalService.announceMarkOnMap).toHaveBeenCalledWith(component.culturalOffer);
  });

  it('should open dialog when more button clicked', () => {
    fixture.debugElement.query(By.css('button')).triggerEventHandler('click', null);
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CulturalDialogComponent, {...DIALOG_OPTIONS, ...{data: offerMock}});
  });

});
