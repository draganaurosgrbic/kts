import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { CommentService } from 'src/app/comments/services/comment.service';
import { Comment } from 'src/app/models/comment';
import { CommentListComponent } from './comment-list.component';

describe('CommentListComponent', () => {
  let component: CommentListComponent;
  let fixture: ComponentFixture<CommentListComponent>;
  const culturalOfferId = 1;
  const commentsMock: Comment[] = [
    {
      id: 1,
      user: 'user1',
      culturalOfferId,
      createdAt: 'date1',
      rate: 1,
      text: 'text1',
      images: ['http://localhost:8080/image1',
      'http://localhost:8080/image2',
      'http://localhost:8080/image3']
      },
    {
      id: 2,
      user: 'user2',
      culturalOfferId,
      createdAt: 'date2',
      rate: 2,
      text: 'text2',
      images: ['http://localhost:8080/image1',
      'http://localhost:8080/image2',
      'http://localhost:8080/image3']
      },
    {
      id: 3,
      user: 'user3',
      culturalOfferId,
      createdAt: 'date3',
      rate: 3,
      text: 'text3',
      images: ['http://localhost:8080/image1',
      'http://localhost:8080/image2',
      'http://localhost:8080/image3']
    }
  ];
  const fetchResponse = {
    body: commentsMock,
    headers: {
      get: jasmine.createSpy('get').and.returnValue('false')
    }
  };

  beforeEach(async () => {
    const commentServiceMock = {
      list: jasmine.createSpy('list').and.returnValue(of(fetchResponse))
    };
    await TestBed.configureTestingModule({
      declarations: [ CommentListComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        {provide: CommentService, useValue: commentServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentListComponent);
    component = fixture.componentInstance;
    component.fetchPending = false;
    component.culturalOfferId = culturalOfferId;
    component.commentService.refreshData$ = of(culturalOfferId);
    spyOn(component, 'fetchComments').and.callThrough();
    spyOn(component, 'changePage').and.callThrough();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render preloader when pending', () => {
    component.fetchPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-preloader'))).toBeTruthy();
  });

  it('should render no comments', () => {
    component.comments = [];
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('app-empty-container'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('app-cultural-details'))).toBeFalsy();
  });

  it('should render some comments', fakeAsync(() => {
    tick();
    expect(component.changePage).toHaveBeenCalledTimes(2);
    expect(component.changePage).toHaveBeenCalledWith(0);
    expect(component.fetchComments).toHaveBeenCalledTimes(2);
    expect(component.fetchComments).toHaveBeenCalledWith();
    expect(component.commentService.list).toHaveBeenCalledTimes(2);
    expect(component.commentService.list).toHaveBeenCalledWith(component.culturalOfferId, component.pagination.pageNumber);
    expect(component.comments).toEqual(commentsMock);
    expect(component.pagination.firstPage).toBeFalse();
    expect(component.pagination.lastPage).toBeFalse();
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('app-comment-details'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.comment).toEqual(component.comments[0]);
    expect(de[1].nativeElement.comment).toEqual(component.comments[1]);
    expect(de[2].nativeElement.comment).toEqual(component.comments[2]);
  }));

  it('should change page', fakeAsync(() => {
    const page = 3;
    fixture.debugElement.query(By.css('app-paginator')).triggerEventHandler('changedPage', page);
    tick();
    expect(component.changePage).toHaveBeenCalledWith(page);
    expect(component.fetchComments).toHaveBeenCalledWith();
    expect(component.commentService.list).toHaveBeenCalledWith(component.culturalOfferId, page);
  }));

});
