import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { SaveCancelComponent } from './save-cancel.component';

describe('SaveCancelComponent', () => {
  let component: SaveCancelComponent;
  let fixture: ComponentFixture<SaveCancelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SaveCancelComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveCancelComponent);
    component = fixture.componentInstance;
    component.pending = false;
    spyOn(component.cancelled, 'emit');
    spyOn(component.saved, 'emit');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.pending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should emit cancelled event when cancel clicked', () => {
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    expect(component.cancelled.emit).toHaveBeenCalledTimes(1);
    expect(component.cancelled.emit).toHaveBeenCalledWith();
  });

  it('should emit saved event when save clicked', () => {
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    expect(component.saved.emit).toHaveBeenCalledTimes(1);
    expect(component.saved.emit).toHaveBeenCalledWith();
  });

});
