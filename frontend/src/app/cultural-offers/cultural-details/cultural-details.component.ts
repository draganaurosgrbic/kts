import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { CulturalDialogComponent } from '../cultural-dialog/cultural-dialog.component';
import { DIALOG_OPTIONS } from 'src/app/constants/dialog';
import { RateUpdate } from 'src/app/models/rate-update';

@Component({
  selector: 'app-cultural-details',
  templateUrl: './cultural-details.component.html',
  styleUrls: ['./cultural-details.component.sass']
})
export class CulturalDetailsComponent implements OnInit {

  constructor(
    public culturalService: CulturalService,
    public dialog: MatDialog
  ) { }

  @Input() culturalOffer: CulturalOffer;

  showDetails(): void{
    const options: MatDialogConfig = {...DIALOG_OPTIONS, ...{data: this.culturalOffer}};
    this.dialog.open(CulturalDialogComponent, options);
  }

  ngOnInit(): void {
    this.culturalService.updateTotalRate$.subscribe((param: RateUpdate) => {
      if (param.id === this.culturalOffer.id){
        this.culturalOffer.totalRate = param.totalRate;
      }
    });
  }

}
