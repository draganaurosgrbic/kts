import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.sass']
})
export class StarRatingComponent implements OnInit {

  constructor() {}

  @Input() editable;
  @Input() starCount = 5;
  @Input() rate = 0;
  @Output() rated: EventEmitter<number> = new EventEmitter();
  rateArray: number[] = [];

  starClick(index: number): void {
    if (index !== this.rate){
      this.rate = index;
    }
    else{
      --this.rate;
    }
    this.rated.emit(this.rate);
  }

  starIcon(index: number): string {
    if (!this.editable){
      return 'star';
    }
    if (this.rate >= index + 1) {
      return 'star';
    }
    return 'star_border';
  }

  ngOnInit(): void {
    for (let index = 0; index < this.starCount; ++index) {
      this.rateArray.push(index);
    }
  }

}
