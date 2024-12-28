import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { CommentDetailsComponent } from './comment-details.component';
import { CommentService } from 'src/app/comments/services/comment.service';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/shared/services/auth.service';
import { User } from 'src/app/models/user';
import { By } from '@angular/platform-browser';
import { CommentFormComponent } from '../comment-form/comment-form.component';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { of } from 'rxjs';
import { Comment } from 'src/app/models/comment';

describe('CommentDetailsComponent', () => {
  let component: CommentDetailsComponent;
  let fixture: ComponentFixture<CommentDetailsComponent>;
  const userMock: User = {
    id: 1,
    accessToken: 'token1',
    role: 'role1',
    email: 'email1',
    firstName: 'firstName1',
    lastName: 'lastName1',
    image: 'http://localhost:8080/image1'
  };
  const commentMock: Comment = {
    id: 1,
    user: 'user1',
    culturalOfferId: 1,
    createdAt: 'date1',
    rate: 1,
    text: 'text1',
    images: ['http://localhost:8080/image1',
    'http://localhost:8080/image2',
    'http://localhost:8080/image3']
  };

  beforeEach(async () => {
    const authServiceMock = {
      getUser: jasmine.createSpy('getUser').and.returnValue(userMock)
    };
    const commentServiceMock = {
      announceRefreshData: jasmine.createSpy('announceRefreshData')
    };
    const dialogMock = {
      open: jasmine.createSpy('open').and.returnValue({
        afterClosed: jasmine.createSpy('afterClosed').and.returnValue(of(0))
      })
    };
    await TestBed.configureTestingModule({
      declarations: [ CommentDetailsComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: CommentService, useValue: commentServiceMock},
        {provide: MatDialog, useValue: dialogMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentDetailsComponent);
    component = fixture.componentInstance;
    component.comment = commentMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render comment details', () => {
    userMock.email = 'email1';
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-star-rating')).nativeElement.starCount)
    .toEqual(component.comment.rate);
    expect(fixture.debugElement.query(By.css('app-star-rating')).nativeElement.editable)
    .toBeFalsy();
    expect(fixture.debugElement.query(By.css('.text-container')).nativeElement.textContent.trim())
    .toEqual(component.comment.text);
    expect(fixture.debugElement.query(By.css('mat-accordion'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('app-carousel')).nativeElement.images)
    .toEqual(component.comment.images);
    expect(fixture.debugElement.queryAll(By.css('span'))[1].nativeElement.textContent.trim())
    .toEqual(component.comment.user);
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
  });

  it('should render crud buttons', () => {
    userMock.email = 'user1';
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('button'));
    expect(de.length).toEqual(2);
    expect(de[0].nativeElement.textContent.trim()).toEqual('edit');
    expect(de[1].nativeElement.textContent.trim()).toEqual('delete');
  });

  it('should open edit dialog', () => {
    component.edit();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.dialog.open).toHaveBeenCalledWith(CommentFormComponent, {...DIALOG_OPTIONS, ...{data: component.comment}});
  });

  it('should open delete dialog', fakeAsync(() => {
    component.delete();
    tick();
    expect(component.dialog.open).toHaveBeenCalledTimes(1);
    expect(component.commentService.announceRefreshData).toHaveBeenCalledTimes(1);
    expect(component.commentService.announceRefreshData).toHaveBeenCalledWith({id: component.comment.culturalOfferId, totalRate: 0});
  }));

});
