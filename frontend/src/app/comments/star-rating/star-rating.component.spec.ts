import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { StarRatingComponent } from './star-rating.component';

describe('StarRatingComponent', () => {
  let component: StarRatingComponent;
  let fixture: ComponentFixture<StarRatingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StarRatingComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StarRatingComponent);
    component = fixture.componentInstance;
    component.editable = true;
    spyOn(component.rated, 'emit').and.callThrough();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be disabled on non editable mode', () => {
    component.editable = false;
    fixture.detectChanges();
    let de: DebugElement[] = fixture.debugElement.queryAll(By.css('button'));
    expect(de.length).toEqual(5);
    expect(de[0].nativeElement.disabled).toBeTrue();
    expect(de[1].nativeElement.disabled).toBeTrue();
    expect(de[2].nativeElement.disabled).toBeTrue();
    expect(de[3].nativeElement.disabled).toBeTrue();
    expect(de[4].nativeElement.disabled).toBeTrue();
    de = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de.length).toEqual(5);
    expect(de[0].nativeElement.textContent.trim()).toEqual('star');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star');
  });

  it('should render no stars', () => {
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    fixture.detectChanges();
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star_border');
    expect(component.rated.emit).toHaveBeenCalledTimes(2);
    expect(component.rated.emit).toHaveBeenCalledWith(0);
  });

  it('should render one star', () => {
    fixture.debugElement.queryAll(By.css('button'))[0].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('star');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star_border');
    expect(component.rated.emit).toHaveBeenCalledTimes(1);
    expect(component.rated.emit).toHaveBeenCalledWith(1);
  });

  it('should render two star', () => {
    fixture.debugElement.queryAll(By.css('button'))[1].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('star');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star_border');
    expect(component.rated.emit).toHaveBeenCalledTimes(1);
    expect(component.rated.emit).toHaveBeenCalledWith(2);
  });

  it('should render three star', () => {
    fixture.debugElement.queryAll(By.css('button'))[2].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('star');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star_border');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star_border');
    expect(component.rated.emit).toHaveBeenCalledTimes(1);
    expect(component.rated.emit).toHaveBeenCalledWith(3);
  });

  it('should render four star', () => {
    fixture.debugElement.queryAll(By.css('button'))[3].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('star');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star_border');
    expect(component.rated.emit).toHaveBeenCalledTimes(1);
    expect(component.rated.emit).toHaveBeenCalledWith(4);
  });

  it('should render five star', () => {
    fixture.debugElement.queryAll(By.css('button'))[4].triggerEventHandler('click', null);
    fixture.detectChanges();
    const de: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-icon'));
    expect(de[0].nativeElement.textContent.trim()).toEqual('star');
    expect(de[1].nativeElement.textContent.trim()).toEqual('star');
    expect(de[2].nativeElement.textContent.trim()).toEqual('star');
    expect(de[3].nativeElement.textContent.trim()).toEqual('star');
    expect(de[4].nativeElement.textContent.trim()).toEqual('star');
    expect(component.rated.emit).toHaveBeenCalledTimes(1);
    expect(component.rated.emit).toHaveBeenCalledWith(5);
  });

});
