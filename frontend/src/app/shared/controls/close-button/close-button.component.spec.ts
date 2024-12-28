import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { CloseButtonComponent } from './close-button.component';

describe('CloseButtonComponent', () => {
  let component: CloseButtonComponent;
  let fixture: ComponentFixture<CloseButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CloseButtonComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CloseButtonComponent);
    component = fixture.componentInstance;
    spyOn(component.closed, 'emit');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render fixed button', () => {
    component.fixed = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('button')).nativeElement.classList).toContain('fixed');
  });

  it('should emit close event when clicked', () => {
    fixture.debugElement.query(By.css('button')).triggerEventHandler('click', null);
    expect(component.closed.emit).toHaveBeenCalledTimes(1);
    expect(component.closed.emit).toHaveBeenCalledWith();
  });

});
