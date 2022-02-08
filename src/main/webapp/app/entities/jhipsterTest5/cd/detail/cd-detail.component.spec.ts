import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CdDetailComponent } from './cd-detail.component';

describe('Cd Management Detail Component', () => {
  let comp: CdDetailComponent;
  let fixture: ComponentFixture<CdDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CdDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cd: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CdDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CdDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cd on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cd).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
