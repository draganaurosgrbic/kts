import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { NewsDetailsComponent } from './news-details.component';
import { NewsService } from 'src/app/news/services/news.service';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/shared/services/auth.service';
import { User } from 'src/app/models/user';
import { By } from '@angular/platform-browser';
import { NewsFormComponent } from '../news-form/news-form.component';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { of } from 'rxjs';
import { News } from 'src/app/models/news';
import { ADMIN_ROLE, GUEST_ROLE } from 'src/app/constants/roles';

describe('NewsDetailsComponent', () => {
  let component: NewsDetailsComponent;
  let fixture: ComponentFixture<NewsDetailsComponent>;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: GUEST_ROLE,
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1'
  };
  const newsMock: News = {
    id: 1,
    culturalOfferId: 1,
    createdAt: new Date(2021, 1, 1),
    text: 'text1',
    images: ['http://localhost:8080/image1',
    'http://localhost:8080/image2',
    'http://localhost:8080/image3']
  };

  beforeEach(async () => {
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock)
    };
    const newsServiceMock = {
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const dialogMock = {
      open: jasmine.createSpy('open').and.returnValue({
        afterClosed: jasmine.createSpy('afterClosed').and.returnValue(of(0))
      })
    };
    await TestBed.configureTestingModule({
      declarations: [ NewsDetailsComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: NewsService, useValue: newsServiceMock},
        {provide: MatDialog, useValue: dialogMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewsDetailsComponent);
    component = fixture.componentInstance;
    component.news = newsMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render news details', () => {
    userMock.role = GUEST_ROLE;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.text-container')).nativeElement.textContent.trim())
    .toEqual(component.news.text);
    expect(fixture.debugElement.query(By.css('app-carousel')).nativeElement.images)
    .toEqual(component.news.images);
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-bold-text'));
    expect(de.length).toEqual(2);
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
  });

  it('should render crud buttons', () => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('button'));
    expect(de.length).toEqual(2);
    expect(de[0].nativeElement.textContent.trim()).toEqual('edit');
    expect(de[1].nativeElement.textContent.trim()).toEqual('delete');
  });

  it('should open edit dialog', () => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    component.edit();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(NewsFormComponent, {...DIALOG_OPTIONS, ...{data: component.news}});
  });

  it('should open delete dialog', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    component.delete();
    tick();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.newsService.announceRefreshData).toHaveBeenCalledTimes(1);
    expect(component.newsService.announceRefreshData).toHaveBeenCalledWith(component.news.culturalOfferId);
  }));
});
