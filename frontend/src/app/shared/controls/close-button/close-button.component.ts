import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-close-button',
  templateUrl: './close-button.component.html',
  styleUrls: ['./close-button.component.sass']
})
export class CloseButtonComponent implements OnInit {

  constructor() { }

  @Input() fixed: boolean;
  @Output() closed: EventEmitter<null> = new EventEmitter();

  ngOnInit(): void {
  }

}
