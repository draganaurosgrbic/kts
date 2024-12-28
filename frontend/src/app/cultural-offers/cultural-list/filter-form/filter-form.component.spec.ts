import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { FilterParams } from 'src/app/models/filter-params';
import { CulturalService } from '../../services/cultural.service';

import { FilterFormComponent } from './filter-form.component';

describe('FilterFormComponent', () => {
  let component: FilterFormComponent;
  let fixture: ComponentFixture<FilterFormComponent>;
  const nameFilters: string[] = ['name1', 'name2', 'name3'];
  const locationFilters: string[] = ['location1', 'location2', 'location3'];
  const typeFilters: string[] = ['type1', 'type2', 'type3'];

  beforeEach(async () => {
    const culturalServiceMock = {
      filterNames: jasmine.createSpy('filterNames').and.returnValue(of(nameFilters)),
      filterLocations: jasmine.createSpy('filterLocations').and.returnValue(of(locationFilters)),
      filterTypes: jasmine.createSpy('filterTypes').and.returnValue(of(typeFilters)),
      announceFilterData: jasmine.createSpy('announceFilterData')
    };
    await TestBed.configureTestingModule({
      declarations: [ FilterFormComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ReactiveFormsModule, FormsModule, MatAutocompleteModule],
      providers: [
        {provide: CulturalService, useValue: culturalServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterFormComponent);
    component = fixture.componentInstance;
    component.fetchPending = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled when pending', () => {
    component.fetchPending = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('form')).nativeElement.classList).toContain('disabled');
    expect(fixture.debugElement.query(By.css('button'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('app-spinner-button'))).toBeTruthy();
  });

  it('should render name autocomplete', async () => {
    const el = fixture.debugElement.queryAll(By.css('input'))[0].nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'asd';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.culturalService.filterNames).toHaveBeenCalledTimes(1);
    expect(component.culturalService.filterNames).toHaveBeenCalledWith('asd');
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-option'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe(nameFilters[0]);
    expect(de[1].nativeElement.textContent.trim()).toBe(nameFilters[1]);
    expect(de[2].nativeElement.textContent.trim()).toBe(nameFilters[2]);
  });

  it('should render location autocomplete', async () => {
    const el = fixture.debugElement.queryAll(By.css('input'))[1].nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'asd';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.culturalService.filterLocations).toHaveBeenCalledTimes(1);
    expect(component.culturalService.filterLocations).toHaveBeenCalledWith('asd');
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-option'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe(locationFilters[0]);
    expect(de[1].nativeElement.textContent.trim()).toBe(locationFilters[1]);
    expect(de[2].nativeElement.textContent.trim()).toBe(locationFilters[2]);
  });

  it('should render type autocomplete', async () => {
    const el = fixture.debugElement.queryAll(By.css('input'))[2].nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'asd';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.culturalService.filterTypes).toHaveBeenCalledTimes(1);
    expect(component.culturalService.filterTypes).toHaveBeenCalledWith('asd');
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-option'));
    expect(de.length).toBe(3);
    expect(de[0].nativeElement.textContent.trim()).toBe(typeFilters[0]);
    expect(de[1].nativeElement.textContent.trim()).toBe(typeFilters[1]);
    expect(de[2].nativeElement.textContent.trim()).toBe(typeFilters[2]);
  });

  it('should render no name autocomplete', async () => {
    const el = fixture.debugElement.query(By.css('input')).nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'a';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.culturalService.filterNames).not.toHaveBeenCalled();
    expect(fixture.debugElement.query(By.css('mat-option'))).toBeFalsy();
  });

  it('should render no location autocomplete', async () => {
    const el = fixture.debugElement.queryAll(By.css('input'))[1].nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'a';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.culturalService.filterNames).not.toHaveBeenCalled();
    expect(fixture.debugElement.query(By.css('mat-option'))).toBeFalsy();
  });

  it('should render no type autocomplete', async () => {
    const el = fixture.debugElement.queryAll(By.css('input'))[2].nativeElement;
    el.dispatchEvent(new Event('focusin'));
    el.value = 'a';
    el.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.culturalService.filterNames).not.toHaveBeenCalled();
    expect(fixture.debugElement.query(By.css('mat-option'))).toBeFalsy();
  });

  it('should emit announceFilterData', () => {
    const filters: FilterParams = {
      name: 'name1',
      location: 'location1',
      type: 'type1'
    };
    component.filterForm.reset(filters);
    fixture.debugElement.query(By.css('button')).triggerEventHandler('click', null);
    expect(component.culturalService.announceFilterData).toHaveBeenCalledTimes(1);
    expect(component.culturalService.announceFilterData).toHaveBeenCalledWith(filters);
  });

});
