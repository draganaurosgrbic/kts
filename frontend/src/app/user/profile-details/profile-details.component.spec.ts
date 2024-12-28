import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogRef } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { PROFILE_PATH, USER_PATH } from 'src/app/constants/router';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { ProfileDetailsComponent } from './profile-details.component';

describe('ProfileDetailsComponent', () => {
  let component: ProfileDetailsComponent;
  let fixture: ComponentFixture<ProfileDetailsComponent>;
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
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock)
    };
    const routerMock = {
      navigate: jasmine.createSpy('navigate')
    };
    const dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    await TestBed.configureTestingModule({
      declarations: [ ProfileDetailsComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: MatDialogRef, useValue: dialogRefMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render profile details', () => {
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('span'));
    expect(de[0].nativeElement.textContent.trim()).toEqual(userMock.email);
    expect(de[1].nativeElement.textContent.trim()).toEqual(userMock.firstName);
    expect(de[2].nativeElement.textContent.trim()).toEqual(userMock.lastName);
    expect(fixture.debugElement.query(By.css('img')).nativeElement.src).toBe(userMock.image);
  });

  it('should navigate to profile form', () => {
    component.edit();
    expect(component.dialogRef.close).toHaveBeenCalledTimes(1);
    expect(component.dialogRef.close).toHaveBeenCalledWith();
    expect(component.router.navigate).toHaveBeenCalledTimes(1);
    expect(component.router.navigate).toHaveBeenCalledWith([`${USER_PATH}/${PROFILE_PATH}`]);
  });

});
