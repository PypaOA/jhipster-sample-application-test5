import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CdService } from '../service/cd.service';
import { ICd, Cd } from '../cd.model';

import { CdUpdateComponent } from './cd-update.component';

describe('Cd Management Update Component', () => {
  let comp: CdUpdateComponent;
  let fixture: ComponentFixture<CdUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cdService: CdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CdUpdateComponent],
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
      .overrideTemplate(CdUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CdUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cdService = TestBed.inject(CdService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cd: ICd = { id: 456 };

      activatedRoute.data = of({ cd });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cd));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cd>>();
      const cd = { id: 123 };
      jest.spyOn(cdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cd }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cdService.update).toHaveBeenCalledWith(cd);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cd>>();
      const cd = new Cd();
      jest.spyOn(cdService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cd }));
      saveSubject.complete();

      // THEN
      expect(cdService.create).toHaveBeenCalledWith(cd);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cd>>();
      const cd = { id: 123 };
      jest.spyOn(cdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cdService.update).toHaveBeenCalledWith(cd);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
