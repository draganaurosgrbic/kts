import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { Pagination } from 'src/app/models/pagination';

@Component({
  selector: 'app-cultural-list',
  templateUrl: './cultural-list.component.html',
  styleUrls: ['./cultural-list.component.sass']
})
export class CulturalListComponent implements OnInit {

  constructor() { }

  @Input() title: string;
  @Input() fetchPending: boolean;
  @Input() culturalOffers: CulturalOffer[] = [];
  @Input() pagination: Pagination = {firstPage: true, lastPage: true, pageNumber: 0};
  @Output() changedPage: EventEmitter<number> = new EventEmitter();

  ngOnInit(): void {
  }

}
