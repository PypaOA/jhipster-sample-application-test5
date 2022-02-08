import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CdComponent } from '../list/cd.component';
import { CdDetailComponent } from '../detail/cd-detail.component';
import { CdUpdateComponent } from '../update/cd-update.component';
import { CdRoutingResolveService } from './cd-routing-resolve.service';

const cdRoute: Routes = [
  {
    path: '',
    component: CdComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CdDetailComponent,
    resolve: {
      cd: CdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CdUpdateComponent,
    resolve: {
      cd: CdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CdUpdateComponent,
    resolve: {
      cd: CdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cdRoute)],
  exports: [RouterModule],
})
export class CdRoutingModule {}
