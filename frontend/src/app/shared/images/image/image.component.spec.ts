import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ImageComponent } from './image.component';

describe('ImageComponent', () => {
  let component: ImageComponent;
  let fixture: ComponentFixture<ImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImageComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageComponent);
    component = fixture.componentInstance;
    component.image = null;
    spyOn(component.deleted, 'emit');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render default image', () => {
    component.image = null;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('img')).nativeElement.getAttribute('src')).toEqual('../../../assets/noimage.png');
  });

  it('should render custom image', () => {
    component.image = 'http://localhost:8080/image1';
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('img')).nativeElement.src).toEqual(component.image);
  });

  it('should emit deleted event when delete clicked', () => {
    fixture.debugElement.query(By.css('button')).triggerEventHandler('click', null);
    expect(component.deleted.emit).toHaveBeenCalledTimes(1);
    expect(component.deleted.emit).toHaveBeenCalledWith();
  });

});
