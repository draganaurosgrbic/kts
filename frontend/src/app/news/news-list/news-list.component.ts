import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { FIRST_PAGE_HEADER, LAST_PAGE_HEADER } from 'src/app/constants/pagination';
import { News } from 'src/app/models/news';
import { Pagination } from 'src/app/models/pagination';
import { NewsService } from 'src/app/news/services/news.service';

@Component({
  selector: 'app-news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.sass']
})
export class NewsListComponent implements OnInit {

  constructor(
    public newsService: NewsService
  ) { }

  @Input() culturalOfferId: number;
  news: News[];
  fetchPending = true;
  pagination: Pagination = {
    pageNumber: 0,
    firstPage: true,
    lastPage: true
  };

  filterForm: FormGroup = new FormGroup({
    startDate: new FormControl(null),
    endDate: new FormControl(null)
  });

  changePage(value: number): void{
    this.pagination.pageNumber += value;
    this.fetchNews();
  }

  filterData(): void{
    this.pagination.pageNumber = 0;
    this.fetchNews();
  }

  fetchNews(): void{
    this.fetchPending = true;
    this.newsService.filter(this.filterForm.value, this.culturalOfferId, this.pagination.pageNumber).subscribe(
      (data: HttpResponse<News[]>) => {
        this.fetchPending = false;
        if (data){
          this.news = data.body;
          const headers: HttpHeaders = data.headers;
          this.pagination.firstPage =
          headers.get(FIRST_PAGE_HEADER) === 'true' ? true : false;
          this.pagination.lastPage =
          headers.get(LAST_PAGE_HEADER) === 'true' ? true : false;
        }
        else{
          this.news = [];
          this.pagination.firstPage = true;
          this.pagination.lastPage = true;
        }
      }
    );
  }

  ngOnInit(): void {
    this.fetchNews();
    this.newsService.refreshData$.subscribe((culturalOfferId: number) => {
      if (this.culturalOfferId === culturalOfferId){
        this.changePage(0);
      }
    });
  }

}
