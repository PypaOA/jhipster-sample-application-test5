import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICd } from '../cd.model';

@Component({
  selector: 'jhi-cd-detail',
  templateUrl: './cd-detail.component.html',
})
export class CdDetailComponent implements OnInit {
  cd: ICd | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cd }) => {
      this.cd = cd;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
