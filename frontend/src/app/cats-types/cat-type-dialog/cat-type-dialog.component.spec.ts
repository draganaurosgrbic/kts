import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';
import { CatTypeDialogComponent } from './cat-type-dialog.component';

describe('CatTypeDialogComponent', () => {
  let component: CatTypeDialogComponent;
  let fixture: ComponentFixture<CatTypeDialogComponent>;

  beforeEach(async () => {
    const dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    await TestBed.configureTestingModule({
      declarations: [ CatTypeDialogComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: true},
        {provide: MatDialogRef, useValue: dialogRefMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CatTypeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render list', () => {
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-cat-type-list'));
    expect(de.length).toBe(1);
  });

  it('should render form', () => {
    component.cats = true;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-cat-form'));
    expect(de.length).toBe(1);
  });

  it('should render tabs', () => {
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-tab'));
    expect(de.length).toBe(2);
  });
});
