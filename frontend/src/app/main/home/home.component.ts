import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FIRST_PAGE_HEADER, LAST_PAGE_HEADER } from 'src/app/constants/pagination';
import { ADMIN_ROLE, GUEST_ROLE } from 'src/app/constants/roles';
import { CulturalOffer } from 'src/app/models/cultural-offer';
import { FilterParams } from 'src/app/models/filter-params';
import { Pagination } from 'src/app/models/pagination';
import { CulturalService } from 'src/app/cultural-offers/services/cultural.service';
import { UserFollowingService } from 'src/app/cultural-offers/services/user-following.service';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public culturalService: CulturalService,
    public userFollowingService: UserFollowingService
  ) { }

  selectedTab = 0;
  culturalOffers: CulturalOffer[][] = [[], []];
  fetchPending: boolean[] = [true, true];
  pagination: Pagination[] = [
    {pageNumber: 0, firstPage: true, lastPage: true},
    {pageNumber: 0, firstPage: true, lastPage: true}
  ];
  filterParams: FilterParams[] = [
    {name: '', location: '', type: ''},
    {name: '', location: '', type: ''}
  ];

  get guest(): boolean{
    return this.authService.getUser()?.role === GUEST_ROLE;
  }

  get admin(): boolean{
    return this.authService.getUser()?.role === ADMIN_ROLE;
  }

  changeTab(index: number): void{
    this.selectedTab = index;
    if (this.fetchPending[this.selectedTab]){
      this.fetchData();
    }
  }

  changePage(value: number): void{
    this.pagination[this.selectedTab].pageNumber += value;
    this.fetchData();
  }

  fetchData(): void{
    this.fetchPending[this.selectedTab] = true;
    this.culturalOffers[this.selectedTab] = [];
    const selectedService = this.selectedTab ? this.userFollowingService : this.culturalService;
    selectedService.filter(this.filterParams[this.selectedTab], this.pagination[this.selectedTab].pageNumber).subscribe(
      (data: HttpResponse<CulturalOffer[]>) => {
        this.fetchPending[this.selectedTab] = false;
        if (data){
          this.culturalOffers[this.selectedTab] = data.body;
          const headers: HttpHeaders = data.headers;
          this.pagination[this.selectedTab].firstPage =
          headers.get(FIRST_PAGE_HEADER) === 'true' ? true : false;
          this.pagination[this.selectedTab].lastPage =
          headers.get(LAST_PAGE_HEADER) === 'true' ? true : false;
        }
        else{
          this.culturalOffers[this.selectedTab] = [];
          this.pagination[this.selectedTab].firstPage = true;
          this.pagination[this.selectedTab].lastPage = true;
        }
      }
    );
  }

  refreshData(response: CulturalOffer | number): void{
    const temp1: number[] = this.culturalOffers[0].map(co => co.id);
    const temp2: number[] = this.culturalOffers[1].map(co => co.id);
    if (typeof response === 'number'){
      if (this.admin){
        const index: number = temp1.indexOf(response);
        if (index !== -1){
          this.culturalOffers[0].splice(index, 1);
        }
      }
    }
    else{
      if (this.admin){
        const index: number = temp1.indexOf(response.id);
        if (index === -1){
          this.culturalOffers[0].splice(0, 0, response);
        }
        else {
          this.culturalOffers[0].splice(index, 1, response);
        }
      }
      else if (this.guest){
        let index: number = temp1.indexOf(response.id);
        if (index !== -1){
          this.culturalOffers[0].splice(index, 1, response);
        }
        index = temp2.indexOf(response.id);
        if (index === -1){
          this.culturalOffers[1].splice(0, 0, response);
        }
        else {
          this.culturalOffers[1].splice(index, 1);
        }
      }
    }
  }

  ngOnInit(): void {
    this.changePage(0);
    this.culturalService.refreshData$.subscribe((response: CulturalOffer | number) => {
      this.refreshData(response);
    });
    this.culturalService.filterData$.subscribe((filterParams: FilterParams) => {
      this.filterParams[this.selectedTab] = filterParams;
      this.pagination[this.selectedTab].pageNumber = 0;
      this.changePage(0);
    });
  }

}
