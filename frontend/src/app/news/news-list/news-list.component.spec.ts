import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { NewsService } from 'src/app/news/services/news.service';
import { News } from 'src/app/models/news';
import { NewsListComponent } from './news-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';

describe('NewsListComponent', () => {
  let component: NewsListComponent;
  let fixture: ComponentFixture<NewsListComponent>;
  const dateStart = '1/1/2021';
  const dateEnd = '1/2/2021';
  const culturalOfferId = 1;
  const newsMock: News[] = [
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
  const newsMockFilter: News[] = [
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
  ];
  const fetchResponse = {
    body: newsMock,
    headers: {
      get: jasmine.createSpy('get').and.returnValue('false')
    }
  };
  const fetchResponseFilter = {
    body: newsMockFilter,
    headers: {
      get: jasmine.createSpy('get').and.returnValue('false')
    },
  };

  beforeEach(async () => {
    const newsServiceMock = {
      filter: jasmine.createSpy('filter').withArgs({startDate: null, endDate: null }, 1, 0).and.returnValue(of(fetchResponse)).
                                          withArgs({startDate: null, endDate: null }, 1, 3).and.returnValue(of(fetchResponse)).
                                          withArgs({startDate: dateStart, endDate: dateEnd }, 1, 0).and.returnValue(of(fetchResponseFilter))
    };
    await TestBed.configureTestingModule({
      declarations: [ NewsListComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [ReactiveFormsModule, FormsModule, MatDatepickerModule, MatNativeDateModule],
      providers: [
        {provide: NewsService, useValue: newsServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewsListComponent);
    component = fixture.componentInstance;
    component.fetchPending = false;
    component.culturalOfferId = culturalOfferId;
    component.newsService.refreshData$ = of(culturalOfferId);
    spyOn(component, 'fetchNews').and.callThrough();
    spyOn(component, 'changePage').and.callThrough();
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

  it('should render no news', () => {
    component.news = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-news-details'))).toBeFalsy();
  });

  it('should render some news', fakeAsync(() => {
    tick();
    expect(component.changePage).toHaveBeenCalledTimes(1);
    expect(component.changePage).toHaveBeenCalledWith(0);
    expect(component.fetchNews).toHaveBeenCalledTimes(2);
    expect(component.fetchNews).toHaveBeenCalledWith();
    expect(component.newsService.filter).toHaveBeenCalledTimes(2);
    expect(component.newsService.filter).toHaveBeenCalledWith(
      component.filterForm.value, component.culturalOfferId, component.pagination.pageNumber);
    expect(component.news).toEqual(newsMock);
    expect(component.pagination.firstPage).toBeFalse();
    expect(component.pagination.lastPage).toBeFalse();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-news-details'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.news).toEqual(component.news[0]);
    expect(de[1].nativeElement.news).toEqual(component.news[1]);
    expect(de[2].nativeElement.news).toEqual(component.news[2]);
  }));

  it('should change page', fakeAsync(() => {
    const page = 3;
    fixture.debugElement.query(By.css('app-paginator')).triggerEventHandler('changedPage', page);
    tick();
    expect(component.changePage).toHaveBeenCalledWith(page);
    expect(component.fetchNews).toHaveBeenCalledWith();
    expect(component.newsService.filter).toHaveBeenCalledWith(component.filterForm.value, component.culturalOfferId, page);
  }));

  it('should filter the first two news', fakeAsync(() => {
    component.filterForm.setValue({startDate: dateStart, endDate: dateEnd});
    component.fetchNews();
    fixture.detectChanges();
    tick();
    expect(component.changePage).toHaveBeenCalledTimes(1);
    expect(component.changePage).toHaveBeenCalledWith(0);
    expect(component.fetchNews).toHaveBeenCalledTimes(3);
    expect(component.fetchNews).toHaveBeenCalledWith();
    expect(component.newsService.filter).toHaveBeenCalledTimes(3);
    expect(component.newsService.filter).toHaveBeenCalledWith(
      component.filterForm.value, component.culturalOfferId, component.pagination.pageNumber);
    expect(component.news).toEqual(newsMockFilter);
    expect(component.pagination.firstPage).toBeFalse();
    expect(component.pagination.lastPage).toBeFalse();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-news-details'));
    expect(de.length).toBe(2);
    expect(de[0].nativeElement.news).toEqual(component.news[0]);
    expect(de[1].nativeElement.news).toEqual(component.news[1]);
  }));

});
