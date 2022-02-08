import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DvdService } from '../service/dvd.service';
import { IDvd, Dvd } from '../dvd.model';

import { DvdUpdateComponent } from './dvd-update.component';

describe('Dvd Management Update Component', () => {
  let comp: DvdUpdateComponent;
  let fixture: ComponentFixture<DvdUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dvdService: DvdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DvdUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DvdUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DvdUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dvdService = TestBed.inject(DvdService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dvd: IDvd = { id: 456 };

      activatedRoute.data = of({ dvd });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dvd));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dvd>>();
      const dvd = { id: 123 };
      jest.spyOn(dvdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dvd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dvd }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dvdService.update).toHaveBeenCalledWith(dvd);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dvd>>();
      const dvd = new Dvd();
      jest.spyOn(dvdService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dvd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dvd }));
      saveSubject.complete();

      // THEN
      expect(dvdService.create).toHaveBeenCalledWith(dvd);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dvd>>();
      const dvd = { id: 123 };
      jest.spyOn(dvdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dvd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dvdService.update).toHaveBeenCalledWith(dvd);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
