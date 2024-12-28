import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';

import { CenterContainerComponent } from './center-container.component';

@Component({
  template: '<app-center-container>center text</app-center-container>'
})
class TestCenterContainerComponent{}

describe('CenterContainerComponent', () => {
  let component: CenterContainerComponent;
  let fixture: ComponentFixture<CenterContainerComponent>;
  let testFixture: ComponentFixture<TestCenterContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CenterContainerComponent, TestCenterContainerComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CenterContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testFixture = TestBed.createComponent(TestCenterContainerComponent);
    testFixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render center ng-content', () => {
    const el: Element = testFixture.debugElement.query(By.css('div')).nativeElement;
    expect(el.textContent.trim()).toEqual('center text');
    const styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('row');
    expect(styles.justifyContent).toEqual('center');
    expect(styles.alignItems).toEqual('center');
  });

});
