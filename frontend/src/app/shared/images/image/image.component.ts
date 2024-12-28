import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.sass']
})
export class ImageComponent implements OnInit {

  constructor() { }

  @Input() image: string;
  @Output() deleted: EventEmitter<null> = new EventEmitter();

  ngOnInit(): void {
  }

}
