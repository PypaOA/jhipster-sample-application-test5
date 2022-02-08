import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDvd, getDvdIdentifier } from '../dvd.model';

export type EntityResponseType = HttpResponse<IDvd>;
export type EntityArrayResponseType = HttpResponse<IDvd[]>;

@Injectable({ providedIn: 'root' })
export class DvdService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dvds', 'jhipstertest5');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dvd: IDvd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dvd);
    return this.http
      .post<IDvd>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dvd: IDvd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dvd);
    return this.http
      .put<IDvd>(`${this.resourceUrl}/${getDvdIdentifier(dvd) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dvd: IDvd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dvd);
    return this.http
      .patch<IDvd>(`${this.resourceUrl}/${getDvdIdentifier(dvd) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDvd>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDvd[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDvdToCollectionIfMissing(dvdCollection: IDvd[], ...dvdsToCheck: (IDvd | null | undefined)[]): IDvd[] {
    const dvds: IDvd[] = dvdsToCheck.filter(isPresent);
    if (dvds.length > 0) {
      const dvdCollectionIdentifiers = dvdCollection.map(dvdItem => getDvdIdentifier(dvdItem)!);
      const dvdsToAdd = dvds.filter(dvdItem => {
        const dvdIdentifier = getDvdIdentifier(dvdItem);
        if (dvdIdentifier == null || dvdCollectionIdentifiers.includes(dvdIdentifier)) {
          return false;
        }
        dvdCollectionIdentifiers.push(dvdIdentifier);
        return true;
      });
      return [...dvdsToAdd, ...dvdCollection];
    }
    return dvdCollection;
  }

  protected convertDateFromClient(dvd: IDvd): IDvd {
    return Object.assign({}, dvd, {
      added: dvd.added?.isValid() ? dvd.added.toJSON() : undefined,
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
      res.body.forEach((dvd: IDvd) => {
        dvd.added = dvd.added ? dayjs(dvd.added) : undefined;
      });
    }
    return res;
  }
}
