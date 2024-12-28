import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CulturalDialogComponent } from 'src/app/cultural-offers/cultural-dialog/cultural-dialog.component';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.sass']
})
export class MapComponent implements OnInit {

  constructor(
    public culturalService: CulturalService,
    public dialog: MatDialog
  ) { }

  readonly DEFAULT_CENTER: number[] = [44.787197, 20.457273];
  mapPending = true;
  center: CulturalOffer;
  ymaps: any;

  @Input() culturalOffers: CulturalOffer[] = [];
  @Input() fetchPending: boolean;

  get mapCenter(): number[]{
    if (this.center){
      return [this.center.lat, this.center.lng];
    }
    return this.DEFAULT_CENTER;
  }

  markOnMap(culturalOffer: CulturalOffer): void{
    this.center = culturalOffer;
    this.ymaps.instance.balloon.open(this.mapCenter,
      `<div style='text-align: center; font-weight: bold;'>${culturalOffer.name} is placed here!</div>`);
  }

  showDetails(culturalOffer: CulturalOffer): void{
    const options: MatDialogConfig = {...DIALOG_OPTIONS, ...{data: culturalOffer}};
    this.dialog.open(CulturalDialogComponent, options);
  }

  placemarkOptions(culturalOffer: CulturalOffer): any{
    if (!culturalOffer.placemarkIcon){
      return null;
    }
    return {
      iconLayout: 'default#image',
      iconImageHref: culturalOffer.placemarkIcon
    };
  }

  placemarkProperties(culturalOffer: CulturalOffer): any{

    return {
      hintContent: `
        <div style="text-align: center;">
          <div>
            <img src='${culturalOffer.image}' height="100" width="100" alt='no image'>
          </div>
          <div style="font-weight: bold">
          ${culturalOffer.name}
          </div>
          <div>
            ${culturalOffer.location}
          </div>
        </div>
      `
    };
  }

  ngOnInit(): void {
    this.culturalService.markOnMap$.subscribe((culturalOffer: CulturalOffer) => {
      this.markOnMap(culturalOffer);
    });
  }

}
