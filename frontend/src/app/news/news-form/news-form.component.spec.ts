import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NewsService } from '../services/news.service';
import { NewsFormComponent } from './news-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { News } from 'src/app/models/news';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';

describe('NewsFormComponent', () => {
  let component: NewsFormComponent;
  let fixture: ComponentFixture<NewsFormComponent>;
  const newsMock: News = {
    id: 1,
    culturalOfferId: 1,
    createdAt: new Date(2021, 1, 1),
    text: 'text1',
    images: ['http://localhost:8080/image1',
    'http://localhost:8080/image2',
    'http://localhost:8080/image3']
  };

  beforeEach(async () => {
    const newsServiceMock = {
      save: jasmine.createSpy('save'),
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ NewsFormComponent ],
      imports: [
        ReactiveFormsModule,
        FormsModule
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: newsMock},
        {provide: NewsService, useValue: newsServiceMock},
        {provide: MatDialogRef, useValue: dialogRefMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewsFormComponent);
    component = fixture.componentInstance;
    component.savePending = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.savePending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('div')).nativeElement.classList).toContain('disabled');
  });

  it('should communicate with images-input component', () => {
    expect(fixture.debugElement.query(By.css('app-images-input')).nativeElement.images).toEqual(component.images);
  });

  it('should recognize empty text', () => {
    component.text.reset('');
    expect(component.text.invalid).toBeTrue();
    expect(component.text.errors.required).toBeTruthy();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('mat-error')).nativeElement.textContent.trim()).toEqual('Some text is required!');
  });

  it('should recognize blank text', () => {
    component.text.reset('  ');
    expect(component.text.invalid).toBeTrue();
    expect(component.text.errors.pattern).toBeTruthy();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('mat-error')).nativeElement.textContent.trim()).toEqual('Some text is required!');
  });

  it('should recognize valid text', () => {
    component.text.reset('asd');
    expect(component.text.valid).toBeTrue();
  });

  it('should prevent invalid text submit', () => {
    component.text.reset('');
    component.save();
    expect(component.newsService.save).not.toHaveBeenCalled();
  });

  it('should notify invalid save', fakeAsync(() => {
    (component.newsService.save as any).and.returnValue(of(null));
    component.text.reset('asd');
    component.save();
    tick();
    expect(component.newsService.save).toHaveBeenCalledTimes(1);
    expect(component.newsService.save).toHaveBeenCalledWith({
      id: component.news.id,
      culturalOfferId: component.news.culturalOfferId,
      text: component.text.value
    } as News, component.images);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
  }));

  it('should notify valid save', fakeAsync(() => {
    (component.newsService.save as any).and.returnValue(of({}));
    component.text.reset('asd');
    component.save();
    tick();
    expect(component.newsService.save).toHaveBeenCalledTimes(1);
    expect(component.newsService.save).toHaveBeenCalledWith({
      id: component.news.id,
      culturalOfferId: component.news.culturalOfferId,
      text: component.text.value
    } as News, component.images);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith('News successfully published!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith();
    expect(component.newsService.announceRefreshData).toHaveBeenCalledTimes(1);
    expect(component.newsService.announceRefreshData).toHaveBeenCalledWith(component.news.culturalOfferId);
  }));

});
