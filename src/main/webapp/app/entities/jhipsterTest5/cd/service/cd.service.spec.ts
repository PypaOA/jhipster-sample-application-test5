import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { State } from 'app/entities/enumerations/state.model';
import { ICd, Cd } from '../cd.model';

import { CdService } from './cd.service';

describe('Cd Service', () => {
  let service: CdService;
  let httpMock: HttpTestingController;
  let elemDefault: ICd;
  let expectedResult: ICd | ICd[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CdService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      performer: 'AAAAAAA',
      releaseYear: 'AAAAAAA',
      discCount: 'AAAAAAA',
      medium: 'AAAAAAA',
      label: 'AAAAAAA',
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

    it('should create a Cd', () => {
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

      service.create(new Cd()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cd', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          performer: 'BBBBBB',
          releaseYear: 'BBBBBB',
          discCount: 'BBBBBB',
          medium: 'BBBBBB',
          label: 'BBBBBB',
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

    it('should partial update a Cd', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          performer: 'BBBBBB',
          releaseYear: 'BBBBBB',
          label: 'BBBBBB',
          state: 'BBBBBB',
        },
        new Cd()
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

    it('should return a list of Cd', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          performer: 'BBBBBB',
          releaseYear: 'BBBBBB',
          discCount: 'BBBBBB',
          medium: 'BBBBBB',
          label: 'BBBBBB',
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

    it('should delete a Cd', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCdToCollectionIfMissing', () => {
      it('should add a Cd to an empty array', () => {
        const cd: ICd = { id: 123 };
        expectedResult = service.addCdToCollectionIfMissing([], cd);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cd);
      });

      it('should not add a Cd to an array that contains it', () => {
        const cd: ICd = { id: 123 };
        const cdCollection: ICd[] = [
          {
            ...cd,
          },
          { id: 456 },
        ];
        expectedResult = service.addCdToCollectionIfMissing(cdCollection, cd);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cd to an array that doesn't contain it", () => {
        const cd: ICd = { id: 123 };
        const cdCollection: ICd[] = [{ id: 456 }];
        expectedResult = service.addCdToCollectionIfMissing(cdCollection, cd);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cd);
      });

      it('should add only unique Cd to an array', () => {
        const cdArray: ICd[] = [{ id: 123 }, { id: 456 }, { id: 29462 }];
        const cdCollection: ICd[] = [{ id: 123 }];
        expectedResult = service.addCdToCollectionIfMissing(cdCollection, ...cdArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cd: ICd = { id: 123 };
        const cd2: ICd = { id: 456 };
        expectedResult = service.addCdToCollectionIfMissing([], cd, cd2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cd);
        expect(expectedResult).toContain(cd2);
      });

      it('should accept null and undefined values', () => {
        const cd: ICd = { id: 123 };
        expectedResult = service.addCdToCollectionIfMissing([], null, cd, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cd);
      });

      it('should return initial array if no Cd is added', () => {
        const cdCollection: ICd[] = [{ id: 123 }];
        expectedResult = service.addCdToCollectionIfMissing(cdCollection, undefined, null);
        expect(expectedResult).toEqual(cdCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
