import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';
import { EmptyContainerComponent } from './empty-container.component';

@Component({
  template: '<app-empty-container>empty text</app-empty-container>'
})
class TestEmptyContainerComponent{}

describe('EmptyContainerComponent', () => {
  let component: EmptyContainerComponent;
  let fixture: ComponentFixture<EmptyContainerComponent>;
  let testFixture: ComponentFixture<TestEmptyContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmptyContainerComponent, TestEmptyContainerComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmptyContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testFixture = TestBed.createComponent(TestEmptyContainerComponent);
    testFixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render empty ng-content', () => {
    const el: Element = testFixture.debugElement.query(By.css('div')).nativeElement;
    expect(el.textContent.trim()).toEqual('empty text');
    const styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('column');
    expect(styles.justifyContent).toEqual('center');
    expect(styles.alignItems).toEqual('center');
    expect(styles.backgroundColor).toEqual('rgb(255, 255, 255)');
  });

});
