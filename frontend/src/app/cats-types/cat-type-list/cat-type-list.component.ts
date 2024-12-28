import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FIRST_PAGE_HEADER, LAST_PAGE_HEADER } from 'src/app/constants/pagination';
import { Category } from 'src/app/models/category';
import { Pagination } from 'src/app/models/pagination';
import { Type } from 'src/app/models/type';
import { CategoryService } from 'src/app/cats-types/services/category.service';
import { TypeService } from 'src/app/cats-types/services/type.service';

@Component({
  selector: 'app-cat-type-list',
  templateUrl: './cat-type-list.component.html',
  styleUrls: ['./cat-type-list.component.sass']
})
export class CatTypeListComponent implements OnInit {

  constructor(
    public categoryService: CategoryService,
    public typeService: TypeService
  ) { }

  @Input() cats: boolean;
  catTypes: Category[] | Type[] = [];
  fetchPending = true;
  pagination: Pagination = {
    pageNumber: 0,
    firstPage: true,
    lastPage: true
  };

  changePage(value: number): void{
    this.pagination.pageNumber += value;
    this.fetchData();
  }

  fetchData(): void{
    const service = this.cats ? this.categoryService : this.typeService;
    this.fetchPending = true;
    service.list(this.pagination.pageNumber).subscribe(
      (data: HttpResponse<Category[] | Type[]>) => {
        this.fetchPending = false;
        if (data){
          this.catTypes = data.body;
          const headers: HttpHeaders = data.headers;
          this.pagination.firstPage = headers.get(FIRST_PAGE_HEADER) === 'true' ? true : false;
          this.pagination.lastPage = headers.get(LAST_PAGE_HEADER) === 'true' ? true : false;
        }
        else{
          this.catTypes = [];
          this.pagination.firstPage = true;
          this.pagination.lastPage = true;
        }
      }
    );
  }

  ngOnInit(): void {
    this.fetchData();
    if (this.cats){
      this.categoryService.refreshData$.subscribe(() => this.changePage(0));
    }
    else{
      this.typeService.refreshData$.subscribe(() => this.changePage(0));
    }
  }

}
