import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '../../shared.module';
import { SpacerContainerComponent } from './spacer-container.component';

@Component({
  template: '<app-spacer-container>spacer text</app-spacer-container>'
})
class TestSpacerContainerComponent{}

describe('SpacerContainerComponent', () => {
  let component: SpacerContainerComponent;
  let fixture: ComponentFixture<SpacerContainerComponent>;
  let testFixture: ComponentFixture<TestSpacerContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpacerContainerComponent, TestSpacerContainerComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ SharedModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpacerContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testFixture = TestBed.createComponent(TestSpacerContainerComponent);
    testFixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render spacer ng-content', () => {
    const el: Element = testFixture.debugElement.query(By.css('div')).nativeElement;
    expect(el.textContent.trim()).toEqual('spacer text');
    const styles: CSSStyleDeclaration = document.defaultView.getComputedStyle(el, null);
    expect(styles.display).toEqual('flex');
    expect(styles.flexDirection).toEqual('row');
    expect(styles.justifyContent).toEqual('space-between');
    expect(styles.alignItems).toEqual('center');
  });

});
