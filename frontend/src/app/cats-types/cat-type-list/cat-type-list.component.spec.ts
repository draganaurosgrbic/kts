import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { CatTypeListComponent } from './cat-type-list.component';
import { Category } from 'src/app/models/category';
import { Type } from 'src/app/models/type';
import { TypeService } from '../services/type.service';
import { By } from '@angular/platform-browser';

describe('CatTypeListComponent', () => {
  let component: CatTypeListComponent;
  let fixture: ComponentFixture<CatTypeListComponent>;
  const catsMock: Category[] = [
    {
      id: 1,
      name: 'category1'
    },
    {
      id: 2,
      name: 'category2'
    },
    {
      id: 3,
      name: 'category3'
    }
  ];
  const typesMock: Type[] = [
    {
      id: 1,
      name: 'name1',
      category: 'category1',
      placemarkIcon: 'placemarkIcon1',
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
      placemarkIcon: 'placemarkIcon3',

    }
  ];
  const fetchCatsResponse = {
    body: catsMock,
    headers: {
      get: jasmine.createSpy('get').and.returnValue('false')
    }
  };
  const fetchTypesResponse = {
    body: typesMock,
    headers: {
      get: jasmine.createSpy('get').and.returnValue('false')
    }
  };

  beforeEach(async () => {
    const categoryServiceMock = {
      list: jasmine.createSpy('list').and.returnValue(of(fetchCatsResponse))
    };
    const typeServiceMock = {
      list: jasmine.createSpy('list').and.returnValue(of(fetchTypesResponse))
    };
    await TestBed.configureTestingModule({
      declarations: [ CatTypeListComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: CategoryService, useValue: categoryServiceMock},
        {provide: TypeService, useValue: typeServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CatTypeListComponent);
    component = fixture.componentInstance;
    component.cats = true;
    component.fetchPending = false;
    component.categoryService.refreshData$ = of();
    component.typeService.refreshData$ = of();
    spyOn(component, 'fetchData').and.callThrough();
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

  it('should render no cat-types', () => {
    component.catTypes = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-empty-container'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('app-cat-type-details'))).toBeFalsy();
  });

  it('should render some cats', fakeAsync(() => {
    tick();
    expect(component.fetchData).toHaveBeenCalledTimes(1);
    expect(component.fetchData).toHaveBeenCalledWith();
    expect(component.categoryService.list).toHaveBeenCalledTimes(1);
    expect(component.categoryService.list).toHaveBeenCalledWith(component.pagination.pageNumber);
    expect(component.catTypes).toEqual(catsMock);
    expect(component.pagination.firstPage).toBeFalse();
    expect(component.pagination.lastPage).toBeFalse();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-cat-type-details'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.catType).toEqual(component.catTypes[0]);
    expect(de[1].nativeElement.catType).toEqual(component.catTypes[1]);
    expect(de[2].nativeElement.catType).toEqual(component.catTypes[2]);
  }));

  it('should render some types', fakeAsync(() => {
    fixture = TestBed.createComponent(CatTypeListComponent);
    component = fixture.componentInstance;
    component.cats = false;
    component.fetchPending = false;
    component.categoryService.refreshData$ = of();
    component.typeService.refreshData$ = of();
    spyOn(component, 'fetchData').and.callThrough();
    spyOn(component, 'changePage').and.callThrough();
    fixture.detectChanges();
    tick();
    expect(component.fetchData).toHaveBeenCalledTimes(1);
    expect(component.fetchData).toHaveBeenCalledWith();
    expect(component.typeService.list).toHaveBeenCalledTimes(1);
    expect(component.typeService.list).toHaveBeenCalledWith(component.pagination.pageNumber);
    expect(component.catTypes).toEqual(typesMock);
    expect(component.pagination.firstPage).toBeFalse();
    expect(component.pagination.lastPage).toBeFalse();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-cat-type-details'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.catType).toEqual(component.catTypes[0]);
    expect(de[1].nativeElement.catType).toEqual(component.catTypes[1]);
    expect(de[2].nativeElement.catType).toEqual(component.catTypes[2]);
  }));

  it('should change page', fakeAsync(() => {
    const page = 3;
    fixture.debugElement.query(By.css('app-paginator')).triggerEventHandler('changedPage', page);
    tick();
    expect(component.changePage).toHaveBeenCalledWith(page);
    expect(component.fetchData).toHaveBeenCalledWith();
    expect(component.categoryService.list).toHaveBeenCalledWith(page);
  }));

});
