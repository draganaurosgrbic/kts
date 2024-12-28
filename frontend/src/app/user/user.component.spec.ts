import { Location } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatMenuModule } from '@angular/material/menu';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AuthService } from '../shared/services/auth.service';
import { UserComponent } from './user.component';
import { LOGIN_PATH, USER_PATH, REGISTER_PATH } from '../constants/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('UserComponent', () => {
  let component: UserComponent;
  let fixture: ComponentFixture<UserComponent>;
  const routerMock = {
    url: 'url1',
    navigate: jasmine.createSpy('navigate')
  };

  beforeEach(async () => {
    const authServiceMock = {
      deleteUser: jasmine.createSpy('deleteUser')
    };
    const locationMock = {};
    await TestBed.configureTestingModule({
      declarations: [ UserComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [MatMenuModule, BrowserAnimationsModule],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: Location, useValue: locationMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render profile buttons', () => {
    routerMock.url = 'profile';
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toEqual('arrow_back');
    expect(de[1].nativeElement.textContent.trim()).toEqual('more_vert');
    expect(de[2].nativeElement.textContent.trim()).toEqual('logout');
  });

  it('should render login buttons', () => {
    routerMock.url = LOGIN_PATH;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(1);
    expect(de[0].nativeElement.textContent.trim()).toEqual('person_add');
  });

  it('should render register buttons', () => {
    routerMock.url = REGISTER_PATH;
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toBe(1);
    expect(de[0].nativeElement.textContent.trim()).toEqual('login');
  });

  it('should sign out', () => {
    component.signOut();
    expect(component.authService.deleteUser).toHaveBeenCalledTimes(1);
    expect(component.authService.deleteUser).toHaveBeenCalledWith();
    expect(component.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${LOGIN_PATH}`]);
  });

  it('should navigate to register page', () => {
    routerMock.url = LOGIN_PATH;
    fixture.detectChanges();
    component.toggleAuth();
    expect(component.router.navigate).toHaveBeenCalled();
    expect(component.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${REGISTER_PATH}`]);
  });

  it ('should navigate to login page', () => {
    routerMock.url = REGISTER_PATH;
    fixture.detectChanges();
    component.toggleAuth();
    expect(component.router.navigate).toHaveBeenCalled();
    expect(component.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${LOGIN_PATH}`]);
  });

});
