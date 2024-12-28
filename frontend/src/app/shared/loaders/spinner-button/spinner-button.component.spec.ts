import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';
import { SpinnerButtonComponent } from './spinner-button.component';

describe('SpinnerButtonComponent', () => {
  let component: SpinnerButtonComponent;
  let fixture: ComponentFixture<SpinnerButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpinnerButtonComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpinnerButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render spinner button', () => {
    let el: Element = fixture.debugElement.query(By.css('div')).nativeElement;
    let styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('column');
    expect(styles.justifyContent).toEqual('center');
    expect(styles.alignItems).toEqual('center');
    el = fixture.debugElement.query(By.css('mat-card')).nativeElement;
    styles = document.defaultView.getComputedStyle(el, null);
    expect(styles.paddingLeft).toEqual('20px');
    expect(styles.paddingRight).toEqual('20px');
    expect(styles.paddingTop).toEqual('5px');
    expect(styles.paddingBottom).toEqual('5px');
    expect(el.classList).toContain('mat-elevation-z8');
  });

});
