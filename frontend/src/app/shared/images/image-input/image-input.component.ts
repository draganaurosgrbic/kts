import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Image } from 'src/app/models/image';
import { ImageService } from '../../services/image.service';

@Component({
  selector: 'app-image-input',
  templateUrl: './image-input.component.html',
  styleUrls: ['./image-input.component.sass']
})
export class ImageInputComponent implements OnInit {

  constructor(
    public imageService: ImageService
  ) { }

  @Input() profile: boolean;
  @Input() image: string;
  @Output() changed: EventEmitter<Image> = new EventEmitter();
  @Output() removed: EventEmitter<null> = new EventEmitter();

  changeImage(upload: Blob): void{
    this.imageService.getBase64(upload).subscribe(
      (path: string) => {
        this.changed.emit({upload, path});
      }
    );
  }

  ngOnInit(): void {
  }

}
