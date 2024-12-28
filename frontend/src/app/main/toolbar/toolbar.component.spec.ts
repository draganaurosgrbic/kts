import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { CatTypeDialogComponent } from 'src/app/cats-types/cat-type-dialog/cat-type-dialog.component';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { ADMIN_ROLE, GUEST_ROLE } from 'src/app/constants/roles';
import { LOGIN_PATH, USER_PATH } from 'src/app/constants/router';
import { CulturalFormComponent } from 'src/app/cultural-offers/cultural-form/cultural-form.component';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { ProfileDetailsComponent } from 'src/app/user/profile-details/profile-details.component';
import { ToolbarComponent } from './toolbar.component';

describe('ToolbarComponent', () => {
  let component: ToolbarComponent;
  let fixture: ComponentFixture<ToolbarComponent>;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: 'role1',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1'
  };

  beforeEach(async () => {
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock),
      deleteUser: jasmine.createSpy('deleteUser')
    };
    const routerMock = {
      navigate: jasmine.createSpy('navigate')
    };
    const dialogMock = {
      open: jasmine.createSpy('open')
    };
    await TestBed.configureTestingModule({
      declarations: [ ToolbarComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [MatMenuModule, BrowserAnimationsModule],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: MatDialog, useValue: dialogMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ToolbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render user buttons', () => {
    userMock.role = null;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toEqual('menu');
    expect(de[1].nativeElement.textContent.trim()).toEqual('more_vert');
    expect(de[2].nativeElement.textContent.trim()).toEqual('login');
  });

  it('should render admin buttons', () => {
    userMock.role = ADMIN_ROLE;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[2].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(5);
    expect(de[0].nativeElement.textContent.trim()).toEqual('menu');
    expect(de[1].nativeElement.textContent.trim()).toEqual('add');
    expect(de[2].nativeElement.textContent.trim()).toEqual('more_vert');
    expect(de[3].nativeElement.textContent.trim()).toEqual('logout');
    expect(de[4].nativeElement.textContent.trim()).toEqual('category');
  });

  it('should render guest buttons', () => {
    userMock.role = GUEST_ROLE;
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(4);
    expect(de[0].nativeElement.textContent.trim()).toEqual('menu');
    expect(de[1].nativeElement.textContent.trim()).toEqual('more_vert');
    expect(de[2].nativeElement.textContent.trim()).toEqual('logout');
    expect(de[3].nativeElement.textContent.trim()).toEqual('person_outline');
  });

  it('should sign in', () => {
    component.signIn();
    expect(component.router.navigate).toHaveBeenCalledTimes(1);
    expect(component.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${LOGIN_PATH}`]);
  });

  it('should sign out', () => {
    component.signOut();
    expect(component.authService.deleteUser).toHaveBeenCalledTimes(1);
    expect(component.authService.deleteUser).toHaveBeenCalledWith();
    expect(component.router.navigate).toHaveBeenCalledTimes(1);
    expect(component.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${LOGIN_PATH}`]);
  });

  it('should open profile', () => {
    component.profile();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(ProfileDetailsComponent, {
      panelClass: 'no-padding',
      backdropClass: 'cdk-overlay-transparent-backdrop',
      position: {
          top: '30px',
          right: '30px'
      }
    });
  });

  it('should open categories', () => {
    component.categories();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CatTypeDialogComponent,
      {...DIALOG_OPTIONS, ...{width: '600px', height: '600px'}, data: true});
  });

  it('should open types', () => {
    component.types();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CatTypeDialogComponent,
      {...DIALOG_OPTIONS, ...{width: '600px', height: '600px'}, data: false});
  });

  it('should open cultural form', () => {
    component.addOffer();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CulturalFormComponent,
      {...DIALOG_OPTIONS, ...{data: {}}});
  });

});
