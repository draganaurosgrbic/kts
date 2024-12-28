import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-save-cancel',
  templateUrl: './save-cancel.component.html',
  styleUrls: ['./save-cancel.component.sass']
})
export class SaveCancelComponent implements OnInit {

  constructor() { }

  @Input() pending: boolean;
  @Output() cancelled: EventEmitter<null> = new EventEmitter();
  @Output() saved: EventEmitter<null> = new EventEmitter();

  ngOnInit(): void {
  }

}
