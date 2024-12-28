import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable, of, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { AUTOCOMPLETE_DEBOUNCE, AUTOCOMPLETE_LENGTH } from 'src/app/constants/autocomplete';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';

@Component({
  selector: 'app-filter-form',
  templateUrl: './filter-form.component.html',
  styleUrls: ['./filter-form.component.sass']
})
export class FilterFormComponent implements OnInit {

  constructor(
    public culturalService: CulturalService
  ) { }

  @Input() fetchPending: boolean;

  filterForm: FormGroup = new FormGroup({
    name: new FormControl(''),
    location: new FormControl(''),
    type: new FormControl('')
  });

  nameFilters: Subject<string> = new Subject<string>();
  locationFilters: Subject<string> = new Subject<string>();
  typeFilters: Subject<string> = new Subject<string>();

  names: Observable<string[]> = this.nameFilters.pipe(
    debounceTime(AUTOCOMPLETE_DEBOUNCE),
    distinctUntilChanged(),
    switchMap((filter: string) => filter.length >= AUTOCOMPLETE_LENGTH ?
    this.culturalService.filterNames(filter) : of([]))
  );
  locations: Observable<string[]> = this.locationFilters.pipe(
    debounceTime(AUTOCOMPLETE_DEBOUNCE),
    distinctUntilChanged(),
    switchMap((filter: string) => filter.length >= AUTOCOMPLETE_LENGTH ?
    this.culturalService.filterLocations(filter) : of([]))
  );
  types: Observable<string[]> = this.typeFilters.pipe(
    debounceTime(AUTOCOMPLETE_DEBOUNCE),
    distinctUntilChanged(),
    switchMap((filter: string) => filter.length >= AUTOCOMPLETE_LENGTH ?
    this.culturalService.filterTypes(filter) : of([]))
  );

  ngOnInit(): void {
  }

}
