import { NgModule } from '@angular/core';
import { MapComponent } from './map/map.component';
import { SharedModule } from '../shared/shared.module';
import { AngularYandexMapsModule, YA_MAP_CONFIG } from 'angular8-yandex-maps';
import { HomeComponent } from './home/home.component';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { YANDEX_MAP_CONFIG } from '../constants/yandex';
import { CulturalOffersModule } from '../cultural-offers/cultural-offers.module';
import { CatsTypesModule } from '../cats-types/cats-types.module';

@NgModule({
  declarations: [
    ToolbarComponent,
    MapComponent,
    HomeComponent,
  ],
  imports: [
    AngularYandexMapsModule.forRoot(YANDEX_MAP_CONFIG),
    SharedModule,
    CulturalOffersModule,
    CatsTypesModule
  ],
  exports: [
    HomeComponent
  ],
  providers: [{
    provide: YA_MAP_CONFIG,
    useValue: YANDEX_MAP_CONFIG
  }]
})
export class MainModule { }
