import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DvdComponent } from '../list/dvd.component';
import { DvdDetailComponent } from '../detail/dvd-detail.component';
import { DvdUpdateComponent } from '../update/dvd-update.component';
import { DvdRoutingResolveService } from './dvd-routing-resolve.service';

const dvdRoute: Routes = [
  {
    path: '',
    component: DvdComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DvdDetailComponent,
    resolve: {
      dvd: DvdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DvdUpdateComponent,
    resolve: {
      dvd: DvdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DvdUpdateComponent,
    resolve: {
      dvd: DvdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dvdRoute)],
  exports: [RouterModule],
})
export class DvdRoutingModule {}
