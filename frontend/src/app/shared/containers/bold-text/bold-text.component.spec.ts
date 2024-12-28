import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';
import { BoldTextComponent } from './bold-text.component';

@Component({
  template: '<app-bold-text>bold text</app-bold-text>'
})
class TestBoldTextComponent{}

describe('BoldTextComponent', () => {
  let component: BoldTextComponent;
  let fixture: ComponentFixture<BoldTextComponent>;
  let testFixture: ComponentFixture<TestBoldTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoldTextComponent, TestBoldTextComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoldTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testFixture = TestBed.createComponent(TestBoldTextComponent);
    testFixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it ('should render bold ng-content', () => {
    const el: Element = testFixture.debugElement.query(By.css('span')).nativeElement;
    expect(el.textContent.trim()).toEqual('bold text');
    const styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.fontWeight).toEqual('700');
  });

});
