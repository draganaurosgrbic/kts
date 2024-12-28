import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';
import { PreloaderComponent } from './preloader.component';

describe('PreloaderComponent', () => {
  let component: PreloaderComponent;
  let fixture: ComponentFixture<PreloaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreloaderComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreloaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render preloader', () => {
    let el: Element = fixture.debugElement.query(By.css('div')).nativeElement;
    let styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('column');
    expect(styles.justifyContent).toEqual('center');
    expect(styles.alignItems).toEqual('center');
    expect(styles.backgroundColor).toEqual('rgb(255, 255, 255)');
    expect(styles.position).toEqual('absolute');
    expect(styles.zIndex).toEqual('1');
    el = fixture.debugElement.query(By.css('mat-spinner')).nativeElement;
    styles = document.defaultView.getComputedStyle(el, null);
    expect(styles.opacity).toEqual('0.3');
  });

  it('should render transparent preloader', () => {
    component.transparent = true;
    fixture.detectChanges();
    let el: Element = fixture.debugElement.query(By.css('div')).nativeElement;
    let styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('column');
    expect(styles.justifyContent).toEqual('center');
    expect(styles.alignItems).toEqual('center');
    expect(styles.backgroundColor).toEqual('rgba(0, 0, 0, 0)');
    expect(styles.position).toEqual('absolute');
    expect(styles.zIndex).toEqual('1');
    el = fixture.debugElement.query(By.css('mat-spinner')).nativeElement;
    styles = document.defaultView.getComputedStyle(el, null);
    expect(styles.opacity).toEqual('0.3');
  });

});
