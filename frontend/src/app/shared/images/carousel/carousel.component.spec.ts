import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { CarouselComponent } from './carousel.component';

describe('CarouselComponent', () => {
  let component: CarouselComponent;
  let fixture: ComponentFixture<CarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarouselComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render no images', () => {
    component.images = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('img'))).toBeFalsy();
  });

  it('should render some images', () => {
    component.images = [
      'http://localhost:8080/image1',
      'http://localhost:8080/image2',
      'http://localhost:8080/image3'
    ];
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('img'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.src).toEqual(component.images[0]);
    expect(de[1].nativeElement.src).toEqual(component.images[1]);
    expect(de[2].nativeElement.src).toEqual(component.images[2]);
  });

});
