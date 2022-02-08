import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDvd, Dvd } from '../dvd.model';
import { DvdService } from '../service/dvd.service';

@Injectable({ providedIn: 'root' })
export class DvdRoutingResolveService implements Resolve<IDvd> {
  constructor(protected service: DvdService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDvd> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dvd: HttpResponse<Dvd>) => {
          if (dvd.body) {
            return of(dvd.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dvd());
  }
}
