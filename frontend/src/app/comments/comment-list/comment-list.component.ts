import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FIRST_PAGE_HEADER, LAST_PAGE_HEADER } from 'src/app/constants/pagination';
import { Pagination } from 'src/app/models/pagination';
import { CommentService } from 'src/app/comments/services/comment.service';
import { Comment } from 'src/app/models/comment';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.sass']
})
export class CommentListComponent implements OnInit {

  constructor(
    public commentService: CommentService
  ) { }

  @Input() culturalOfferId: number;
  comments: Comment[] = [];
  fetchPending = true;
  pagination: Pagination = {
    pageNumber: 0,
    firstPage: true,
    lastPage: true
  };

  changePage(value: number): void{
    this.pagination.pageNumber += value;
    this.fetchComments();
  }

  fetchComments(): void{
    this.fetchPending = true;
    this.commentService.list(this.culturalOfferId, this.pagination.pageNumber).subscribe(
      (data: HttpResponse<Comment[]>) => {
        this.fetchPending = false;
        if (data){
          this.comments = data.body;
          const headers: HttpHeaders = data.headers;
          this.pagination.firstPage = headers.get(FIRST_PAGE_HEADER) === 'true' ? true : false;
          this.pagination.lastPage = headers.get(LAST_PAGE_HEADER) === 'true' ? true : false;
        }
        else{
          this.comments = [];
          this.pagination.firstPage = true;
          this.pagination.lastPage = true;
        }
      }
    );
  }

  ngOnInit(): void {
    this.changePage(0);
    this.commentService.refreshData$.subscribe((culturalOfferId: number) => {
      if (culturalOfferId === this.culturalOfferId){
        this.changePage(0);
      }
    });
  }

}
