import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { PaginatorComponent } from './paginator.component';

describe('PaginatorComponent', () => {
  let component: PaginatorComponent;
  let fixture: ComponentFixture<PaginatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaginatorComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaginatorComponent);
    component = fixture.componentInstance;
    component.pending = false;
    spyOn(component.changedPage, 'emit');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render fixed paginator', () => {
    component.fixed = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('div')).nativeElement.classList).toContain('fixed');
  });

  it('should render title', () => {
    component.title = 'title1';
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.title')).nativeElement.textContent.trim()).toEqual('title1');
  });

  it('should be disabled when pending', () => {
    component.pending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
  });

  it('should render only left arrow', () => {
    component.pagination = {firstPage: false, lastPage: true, pageNumber: 0};
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.left-arrow button'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('.right-arrow button'))).toBeFalsy();
  });

  it('should render only right arrow', () => {
    component.pagination = {firstPage: true, lastPage: false, pageNumber: 0};
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.left-arrow button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('.right-arrow button'))).toBeTruthy();
  });

  it('should render both arrows', () => {
    component.pagination = {firstPage: false, lastPage: false, pageNumber: 0};
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.left-arrow button'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('.right-arrow button'))).toBeTruthy();
  });

  it('should emit -1 when left arrow clicked', () => {
    component.pagination = {firstPage: false, lastPage: true, pageNumber: 0};
    fixture.detectChanges();
    fixture.debugElement.query(By.css('.left-arrow button')).triggerEventHandler('click', null);
    expect(component.changedPage.emit).toHaveBeenCalledTimes(1);
    expect(component.changedPage.emit).toHaveBeenCalledWith(-1);
  });

  it('should emit 1 when right arrow clicked', () => {
    component.pagination = {firstPage: true, lastPage: false, pageNumber: 0};
    fixture.detectChanges();
    fixture.debugElement.query(By.css('.right-arrow button')).triggerEventHandler('click', null);
    expect(component.changedPage.emit).toHaveBeenCalledTimes(1);
    expect(component.changedPage.emit).toHaveBeenCalledWith(1);
  });

});
