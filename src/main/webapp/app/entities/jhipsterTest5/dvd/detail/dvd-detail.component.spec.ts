import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DvdDetailComponent } from './dvd-detail.component';

describe('Dvd Management Detail Component', () => {
  let comp: DvdDetailComponent;
  let fixture: ComponentFixture<DvdDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DvdDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dvd: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DvdDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DvdDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dvd on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dvd).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
