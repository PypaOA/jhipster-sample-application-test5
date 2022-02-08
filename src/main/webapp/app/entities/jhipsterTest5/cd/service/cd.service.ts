import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICd, getCdIdentifier } from '../cd.model';

export type EntityResponseType = HttpResponse<ICd>;
export type EntityArrayResponseType = HttpResponse<ICd[]>;

@Injectable({ providedIn: 'root' })
export class CdService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cds', 'jhipstertest5');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cd: ICd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cd);
    return this.http
      .post<ICd>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cd: ICd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cd);
    return this.http
      .put<ICd>(`${this.resourceUrl}/${getCdIdentifier(cd) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cd: ICd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cd);
    return this.http
      .patch<ICd>(`${this.resourceUrl}/${getCdIdentifier(cd) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICd>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICd[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCdToCollectionIfMissing(cdCollection: ICd[], ...cdsToCheck: (ICd | null | undefined)[]): ICd[] {
    const cds: ICd[] = cdsToCheck.filter(isPresent);
    if (cds.length > 0) {
      const cdCollectionIdentifiers = cdCollection.map(cdItem => getCdIdentifier(cdItem)!);
      const cdsToAdd = cds.filter(cdItem => {
        const cdIdentifier = getCdIdentifier(cdItem);
        if (cdIdentifier == null || cdCollectionIdentifiers.includes(cdIdentifier)) {
          return false;
        }
        cdCollectionIdentifiers.push(cdIdentifier);
        return true;
      });
      return [...cdsToAdd, ...cdCollection];
    }
    return cdCollection;
  }

  protected convertDateFromClient(cd: ICd): ICd {
    return Object.assign({}, cd, {
      added: cd.added?.isValid() ? cd.added.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.added = res.body.added ? dayjs(res.body.added) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cd: ICd) => {
        cd.added = cd.added ? dayjs(cd.added) : undefined;
      });
    }
    return res;
  }
}
