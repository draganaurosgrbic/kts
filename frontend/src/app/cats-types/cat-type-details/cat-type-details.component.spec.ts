import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { CatTypeDetailsComponent } from './cat-type-details.component';
import { MatDialog } from '@angular/material/dialog';
import { Category } from 'src/app/models/category';
import { By } from '@angular/platform-browser';
import { TypeService } from '../services/type.service';
import { of } from 'rxjs';

describe('CatTypeDetailsComponent', () => {
  let component: CatTypeDetailsComponent;
  let fixture: ComponentFixture<CatTypeDetailsComponent>;
  const catTypeMock: Category = {
    id: 1,
    name: 'name1'
  };

  beforeEach(async () => {
    const categoryServiceMock = {
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const typeServiceMock = {
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const dialogMock = {
      open: jasmine.createSpy('open').and.returnValue({
        afterClosed: jasmine.createSpy('afterClosed').and.returnValue(of(1))
      })
    };
    await TestBed.configureTestingModule({
      declarations: [ CatTypeDetailsComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: CategoryService, useValue: categoryServiceMock},
        {provide: TypeService, useValue: typeServiceMock},
        {provide: MatDialog, useValue: dialogMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CatTypeDetailsComponent);
    component = fixture.componentInstance;
    component.catType = catTypeMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render catType details', () => {
    expect(fixture.debugElement.query(By.css('span')).nativeElement.textContent.trim())
    .toEqual(component.catType.name);
  });

  it('should open delete dialog', fakeAsync(() => {
    component.delete();
    tick();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.categoryService.announceRefreshData).toHaveBeenCalledTimes(1);
  }));
});
