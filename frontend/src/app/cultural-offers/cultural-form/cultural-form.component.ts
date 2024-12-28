import { Component, ElementRef, Inject, AfterViewInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, of, Subject } from 'rxjs';
import places, { PlacesInstance } from 'places.js';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { TypeService } from 'src/app/cats-types/services/type.service';
import { CulturalValidatorService } from 'src/app/cultural-offers/services/cultural-validator.service';
import { TypeValidatorService } from 'src/app/cats-types/services/type-validator.service';
import { Geolocation } from 'src/app/models/geolocation';
import { Image } from 'src/app/models/image';
import { SNACKBAR_ERROR, SNACKBAR_ERROR_OPTIONS, SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS } from 'src/app/constants/snackbar';
import { ALGOLIA_API_ID, ALGOLIA_API_KEY } from 'src/app/constants/algolia';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { AUTOCOMPLETE_DEBOUNCE, AUTOCOMPLETE_LENGTH } from 'src/app/constants/autocomplete';

@Component({
  selector: 'app-cultural-form',
  templateUrl: './cultural-form.component.html',
  styleUrls: ['./cultural-form.component.sass']
})
export class CulturalFormComponent implements AfterViewInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public culturalOffer: CulturalOffer,
    public culturalService: CulturalService,
    public typeService: TypeService,
    public culturalValidator: CulturalValidatorService,
    public typeValidator: TypeValidatorService,
    public dialogRef: MatDialogRef<CulturalFormComponent>,
    public snackBar: MatSnackBar
  ) { }

  geolocation: Geolocation = {
    lat: this.culturalOffer.lat,
    lng: this.culturalOffer.lng
  };

  culturalForm: FormGroup = new FormGroup({
    type: new FormControl(this.culturalOffer.type || '',
    [Validators.required, Validators.pattern(new RegExp('\\S'))], [this.typeValidator.hasName(false)]),
    name: new FormControl(this.culturalOffer.name || '',
    [Validators.required, Validators.pattern(new RegExp('\\S'))], [this.culturalValidator.hasName(this.culturalOffer.id)]),
    location: new FormControl(this.culturalOffer.location || '',
    [Validators.required, Validators.pattern(new RegExp('\\S')), this.culturalValidator.locationFound(this.geolocation)]),
    description: new FormControl(this.culturalOffer.description || '')
  });

  savePending = false;
  image: Image = {upload: null, path: this.culturalOffer.image};
  typeFilters: Subject<string> = new Subject<string>();
  types: Observable<string[]> = this.typeFilters.pipe(
    debounceTime(AUTOCOMPLETE_DEBOUNCE),
    distinctUntilChanged(),
    switchMap((filter: string) => filter.length >= AUTOCOMPLETE_LENGTH ?
    this.typeService.filterNames(filter) : of([]))
  );

  @ViewChild('locationInput') locationInput: ElementRef<HTMLInputElement>;
  locationAutocomplete: PlacesInstance;

  save(): void{

    if (this.culturalForm.invalid){
      return;
    }

    const offer: CulturalOffer = {
      id: this.culturalOffer.id,
      type: this.culturalForm.value.type,
      name: this.culturalForm.value.name,
      location: this.culturalForm.value.location,
      lat: this.geolocation.lat,
      lng: this.geolocation.lng,
      description: this.culturalForm.value.description
    } as CulturalOffer;

    this.savePending = true;
    this.culturalService.save(offer, this.image).subscribe(
      (culturalOffer: CulturalOffer) => {
        this.savePending = false;
        if (culturalOffer){
          this.snackBar.open('Offer successfully saved!', SNACKBAR_CLOSE, SNACKBAR_SUCCESS_OPTIONS);
          this.dialogRef.close(true);
          this.culturalService.announceRefreshData(culturalOffer);
          this.culturalService.announceMarkOnMap(culturalOffer);
        }
        else{
          this.snackBar.open(SNACKBAR_ERROR, SNACKBAR_CLOSE, SNACKBAR_ERROR_OPTIONS);
        }
      }
    );

  }

  ngAfterViewInit(): void{
    this.locationAutocomplete = places({
      container: this.locationInput.nativeElement,
      appId: ALGOLIA_API_ID,
      apiKey: ALGOLIA_API_KEY
    });

    this.locationAutocomplete.on('change', event => {
      this.geolocation.lat = event.suggestion.latlng.lat;
      this.geolocation.lng = event.suggestion.latlng.lng;
      this.culturalForm.get('location').setValue(event.suggestion.value);
      this.culturalForm.get('location').updateValueAndValidity();
    });
  }

}
