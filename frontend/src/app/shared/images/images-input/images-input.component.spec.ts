import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { ImageService } from '../../services/image.service';

import { ImagesInputComponent } from './images-input.component';

describe('ImagesInputComponent', () => {
  let component: ImagesInputComponent;
  let fixture: ComponentFixture<ImagesInputComponent>;

  beforeEach(async () => {
    const imageServiceMock = {
      getBase64: jasmine.createSpy('getBase64').and.returnValue(of(null))
    };
    await TestBed.configureTestingModule({
      declarations: [ ImagesInputComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      providers: [
        {provide: ImageService, useValue: imageServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImagesInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render no images', () => {
    component.images = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-image'))).toBeFalsy();
  });

  it('should dispaly some images', () => {
    component.images = [
      {path: 'http://localhost:8080/image1'},
      {path: 'http://localhost:8080/image2'},
      {path: 'http://localhost:8080/image3'}
    ];
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-image'));
    expect(de.length).toEqual(3);
    expect(de[0].nativeElement.image).toEqual(component.images[0].path);
    expect(de[1].nativeElement.image).toEqual(component.images[1].path);
    expect(de[2].nativeElement.image).toEqual(component.images[2].path);
  });

  it('should add image', fakeAsync(() => {
    component.addImage(new Blob());
    tick();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-image'));
    expect(de.length).toEqual(1);
    expect(de[0].nativeElement.image).toBeNull();
  }));

});
