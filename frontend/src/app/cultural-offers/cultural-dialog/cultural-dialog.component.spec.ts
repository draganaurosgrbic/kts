import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { CommentFormComponent } from 'src/app/comments/comment-form/comment-form.component';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { ADMIN_ROLE, GUEST_ROLE } from 'src/app/constants/roles';
import { SNACKBAR_CLOSE, SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS } from 'src/app/constants/snackbar';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { UserFollowingService } from 'src/app/cultural-offers/services/user-following.service';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { User } from 'src/app/models/user';
import { NewsFormComponent } from 'src/app/news/news-form/news-form.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { CulturalFormComponent } from '../cultural-form/cultural-form.component';
import { CulturalDialogComponent } from './cultural-dialog.component';

describe('CulturalDialogComponent', () => {
  let component: CulturalDialogComponent;
  let fixture: ComponentFixture<CulturalDialogComponent>;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: 'role1',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1',
  };
  const offerMock: CulturalOffer = {
    id: 1,
    category: 'category1',
    type: 'type1',
    placemarkIcon: 'placemark1',
    name: 'name1',
    location: 'location1',
    lat: 1,
    lng: 1,
    description: 'description1',
    image: 'http://localhost:8080/image1',
    followed: true,
    totalRate: 1
  };

  beforeEach(async () => {
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock)
    };
    const culturalServiceMock = {
      delete: jasmine.createSpy('delete'),
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const userFollowingService = {
      toggleSubscription: jasmine.createSpy('toggleSubscription')
    };
    const dialogMock = {
      open: jasmine.createSpy('open').and.returnValue({
        afterClosed: jasmine.createSpy('afterClosed').and.returnValue(of(true))
      })
    };
    const dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    const snackBarMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ CulturalDialogComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [MatButtonModule],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: offerMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: CulturalService, useValue: culturalServiceMock},
        {provide: UserFollowingService, useValue: userFollowingService},
        {provide: MatDialog, useValue: dialogMock},
        {provide: MatDialogRef, useValue: dialogRefMock},
        {provide: MatSnackBar, useValue: snackBarMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalDialogComponent);
    component = fixture.componentInstance;
    component.toggleSubPending = false;
    component.culturalService.updateTotalRate$ = of({id: offerMock.id, totalRate: 0});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should subscribe to updateTotalRate$', fakeAsync(() => {
    tick();
    expect(component.culturalOffer.totalRate).toEqual(0);
  }));

  it('should render cultural details', () => {
    expect(fixture.debugElement.query(By.css('app-bold-text')).nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.name);
    expect(fixture.debugElement.queryAll(By.css('span'))[0].nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.type + ', ' + component.culturalOffer.category);
    expect(fixture.debugElement.queryAll(By.css('span'))[1].nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.location);
    expect(fixture.debugElement.queryAll(By.css('span'))[2].nativeElement.textContent.trim())
    .toEqual(component.culturalOffer.description);
    expect(fixture.debugElement.queryAll(By.css('span'))[3].nativeElement.textContent)
    .toEqual(component.culturalOffer.totalRate + '.00/5');
    expect(fixture.debugElement.query(By.css('img')).nativeElement.src)
    .toEqual(component.culturalOffer.image);
  });

  it('should render admin buttons', () => {
    userMock.role = ADMIN_ROLE;
    fixture.debugElement.queryAll(By.css('mat-tab'))[1].nativeElement.isActive = true;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(4);
    expect(de[0].nativeElement.textContent.trim()).toBe('edit');
    expect(de[1].nativeElement.textContent.trim()).toBe('delete');
    expect(de[2].nativeElement.textContent.trim()).toBe('rate_review');
    expect(de[3].nativeElement.textContent.trim()).toBe('event_note');
  });

  it('should render guest buttons', () => {
    userMock.role = GUEST_ROLE;
    offerMock.followed = true;
    fixture.debugElement.queryAll(By.css('mat-tab'))[0].nativeElement.isActive = true;
    fixture.detectChanges();

    let de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe('unsubscribe');
    expect(de[1].nativeElement.textContent.trim()).toBe('rate_review');
    expect(de[2].nativeElement.textContent.trim()).toBe('insert_comment');

    offerMock.followed = false;
    fixture.detectChanges();

    de = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe('email');
    expect(de[1].nativeElement.textContent.trim()).toBe('rate_review');
    expect(de[2].nativeElement.textContent.trim()).toBe('insert_comment');

    component.toggleSubPending = true;
    fixture.detectChanges();

    de = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(2);
    expect(de[0].nativeElement.textContent.trim()).toBe('rate_review');
    expect(de[1].nativeElement.textContent.trim()).toBe('insert_comment');
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should open edit dialog', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    tick();

    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CulturalFormComponent, {...DIALOG_OPTIONS, ...{data: component.culturalOffer}});
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith();
  }));

  it('should open delete dialog', fakeAsync(() => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    tick();

    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith();
    expect(component.culturalService.announceRefreshData).toHaveBeenCalledTimes(1);
    expect(component.culturalService.announceRefreshData).toHaveBeenCalledWith(component.culturalOffer.id);
  }));

  it('should open comment dialog', () => {
    userMock.role = GUEST_ROLE;
    fixture.debugElement.queryAll(By.css('mat-tab'))[0].nativeElement.isActive = true;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[2].triggerEventHandler('click', null);
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CommentFormComponent,
      {...DIALOG_OPTIONS, ...{data: {culturalOfferId: component.culturalOffer.id, images: []}}});
  });

  it('should open news dialog', () => {
    userMock.role = ADMIN_ROLE;
    fixture.debugElement.queryAll(By.css('mat-tab'))[1].nativeElement.isActive = true;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[3].triggerEventHandler('click', null);
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(NewsFormComponent,
      {...DIALOG_OPTIONS, ...{data: {culturalOfferId: component.culturalOffer.id, images: []}}});
  });

  it('should notify valid subscription toggle', fakeAsync(() => {
    (component.userFollowingService.toggleSubscription as any).and.returnValue(of(true));
    userMock.role = GUEST_ROLE;
    offerMock.followed = true;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    tick();

    expect(component.userFollowingService.toggleSubscription).toHaveBeenCalledTimes(1);
    expect(component.userFollowingService.toggleSubscription).toHaveBeenCalledWith(component.culturalOffer.id);
    expect(component.culturalService.announceRefreshData).toHaveBeenCalledTimes(1);
    expect(component.culturalService.announceRefreshData).toHaveBeenCalledWith(component.culturalOffer);
    expect(offerMock.followed).toBeFalse();
  }));

  it('should notify invalid subscription toggle', fakeAsync(() => {
    (component.userFollowingService.toggleSubscription as any).and.returnValue(of(false));
    userMock.role = GUEST_ROLE;
    offerMock.followed = true;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    tick();

    expect(component.userFollowingService.toggleSubscription).toHaveBeenCalledTimes(1);
    expect(component.userFollowingService.toggleSubscription).toHaveBeenCalledWith(component.culturalOffer.id);
    expect(component.snackBar.open).toHaveBeenCalledTimes(1);
    expect(component.snackBar.open).toHaveBeenCalledWith(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
    expect(offerMock.followed).toBeTrue();
  }));

});
