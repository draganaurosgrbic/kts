import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Pagination } from 'src/app/models/pagination';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.sass']
})
export class PaginatorComponent implements OnInit {

  constructor() { }

  @Input() fixed: boolean;
  @Input() title: string;
  @Input() pending: boolean;
  @Input() pagination: Pagination = {firstPage: true, lastPage: true, pageNumber: 0};
  @Output() changedPage: EventEmitter<number> = new EventEmitter();

  ngOnInit(): void {
  }

}
