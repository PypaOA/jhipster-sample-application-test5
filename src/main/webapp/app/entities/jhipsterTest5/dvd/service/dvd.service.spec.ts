import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { State } from 'app/entities/enumerations/state.model';
import { IDvd, Dvd } from '../dvd.model';

import { DvdService } from './dvd.service';

describe('Dvd Service', () => {
  let service: DvdService;
  let httpMock: HttpTestingController;
  let elemDefault: IDvd;
  let expectedResult: IDvd | IDvd[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DvdService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      performer: 'AAAAAAA',
      releaseYear: 'AAAAAAA',
      discCount: 'AAAAAAA',
      format: 'AAAAAAA',
      lang: 'AAAAAAA',
      state: State.OK,
      added: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          added: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dvd', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          added: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          added: currentDate,
        },
        returnedFromService
      );

      service.create(new Dvd()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dvd', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          performer: 'BBBBBB',
          releaseYear: 'BBBBBB',
          discCount: 'BBBBBB',
          format: 'BBBBBB',
          lang: 'BBBBBB',
          state: 'BBBBBB',
          added: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          added: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dvd', () => {
      const patchObject = Object.assign(
        {
          performer: 'BBBBBB',
          discCount: 'BBBBBB',
        },
        new Dvd()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          added: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dvd', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          performer: 'BBBBBB',
          releaseYear: 'BBBBBB',
          discCount: 'BBBBBB',
          format: 'BBBBBB',
          lang: 'BBBBBB',
          state: 'BBBBBB',
          added: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          added: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dvd', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDvdToCollectionIfMissing', () => {
      it('should add a Dvd to an empty array', () => {
        const dvd: IDvd = { id: 123 };
        expectedResult = service.addDvdToCollectionIfMissing([], dvd);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dvd);
      });

      it('should not add a Dvd to an array that contains it', () => {
        const dvd: IDvd = { id: 123 };
        const dvdCollection: IDvd[] = [
          {
            ...dvd,
          },
          { id: 456 },
        ];
        expectedResult = service.addDvdToCollectionIfMissing(dvdCollection, dvd);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dvd to an array that doesn't contain it", () => {
        const dvd: IDvd = { id: 123 };
        const dvdCollection: IDvd[] = [{ id: 456 }];
        expectedResult = service.addDvdToCollectionIfMissing(dvdCollection, dvd);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dvd);
      });

      it('should add only unique Dvd to an array', () => {
        const dvdArray: IDvd[] = [{ id: 123 }, { id: 456 }, { id: 9410 }];
        const dvdCollection: IDvd[] = [{ id: 123 }];
        expectedResult = service.addDvdToCollectionIfMissing(dvdCollection, ...dvdArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dvd: IDvd = { id: 123 };
        const dvd2: IDvd = { id: 456 };
        expectedResult = service.addDvdToCollectionIfMissing([], dvd, dvd2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dvd);
        expect(expectedResult).toContain(dvd2);
      });

      it('should accept null and undefined values', () => {
        const dvd: IDvd = { id: 123 };
        expectedResult = service.addDvdToCollectionIfMissing([], null, dvd, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dvd);
      });

      it('should return initial array if no Dvd is added', () => {
        const dvdCollection: IDvd[] = [{ id: 123 }];
        expectedResult = service.addDvdToCollectionIfMissing(dvdCollection, undefined, null);
        expect(expectedResult).toEqual(dvdCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
