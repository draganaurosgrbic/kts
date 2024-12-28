import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { ImageService } from '../../services/image.service';
import { ImageInputComponent } from './image-input.component';

describe('ImageInputComponent', () => {
  let component: ImageInputComponent;
  let fixture: ComponentFixture<ImageInputComponent>;

  beforeEach(async () => {
    const imageServiceMock = {
      getBase64: jasmine.createSpy('getBase64').and.returnValue(of(null))
    };
    await TestBed.configureTestingModule({
      declarations: [ ImageInputComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      providers: [
        {provide: ImageService, useValue: imageServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageInputComponent);
    component = fixture.componentInstance;
    component.image = null;
    spyOn(component.removed, 'emit');
    spyOn(component.changed, 'emit');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render default profile image', () => {
    component.profile = true;
    component.image = null;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('img')).nativeElement.getAttribute('src'))
    .toEqual('../../../assets/noprofile.png');
  });

  it('should render default non profile image', () => {
    component.profile = false;
    component.image = null;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('img')).nativeElement.getAttribute('src'))
    .toEqual('../../../assets/noimage.png');
  });

  it('should render custom image', () => {
    component.image = 'http://localhost:8080/image1';
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('img')).nativeElement.src)
    .toEqual(component.image);
  });

  it('should render add button', () => {
    component.image = null;
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('button'))[0].nativeElement.textContent.trim())
    .toEqual('Add image');
  });

  it('should render change button', () => {
    component.image = 'http://localhost:8080/image1';
    fixture.detectChanges();
    expect(fixture.debugElement.queryAll(By.css('button'))[0].nativeElement.textContent.trim())
    .toEqual('Change image');
  });

  it('should not render remove button', () => {
    component.image = null;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('button'));
    expect(de.length).toBe(1);
    expect(de[0].nativeElement.textContent.trim()).not.toEqual('Remove image');
  });

  it('should render remove button', () => {
    component.image = 'http://localhost:8080/image1';
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('button'));
    expect(de.length).toBe(2);
    expect(de[1].nativeElement.textContent.trim()).toEqual('Remove image');
  });

  it('should emit removed event when remove clicked', () => {
    component.image = 'http://localhost:8080/image1';
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    expect(component.removed.emit).toHaveBeenCalledTimes(1);
    expect(component.removed.emit).toHaveBeenCalledWith();
  });

  it('should emit changed event when image changed', fakeAsync(() => {
    const upload: Blob = new Blob();
    component.changeImage(upload);
    tick();
    expect(component.changed.emit).toHaveBeenCalledTimes(1);
    expect(component.changed.emit).toHaveBeenCalledWith({upload, path: null});
  }));

});
