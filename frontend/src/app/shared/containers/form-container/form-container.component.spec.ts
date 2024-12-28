import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';
import { FormContainerComponent } from './form-container.component';

@Component({
  template: '<app-form-container>form text</app-form-container>'
})
class TestFormContainerComponent{}

describe('FormContainerComponent', () => {
  let component: FormContainerComponent;
  let fixture: ComponentFixture<FormContainerComponent>;
  let testFixture: ComponentFixture<TestFormContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormContainerComponent, TestFormContainerComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testFixture = TestBed.createComponent(TestFormContainerComponent);
    testFixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render form ng-content', () => {
    let el: Element = testFixture.debugElement.query(By.css('div')).nativeElement;
    expect(el.textContent.trim()).toEqual('form text');
    let styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('column');
    expect(styles.justifyContent).toEqual('center');
    expect(styles.alignItems).toEqual('center');
    expect(styles.padding).toEqual('30px');
    el = testFixture.debugElement.query(By.css('mat-card')).nativeElement;
    styles = document.defaultView.getComputedStyle(el, null);
    expect(styles.minWidth).toEqual('400px');
    expect(el.classList).toContain('mat-elevation-z24');
  });

});
