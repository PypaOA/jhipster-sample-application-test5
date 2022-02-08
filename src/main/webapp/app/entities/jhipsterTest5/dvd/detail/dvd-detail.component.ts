import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDvd } from '../dvd.model';

@Component({
  selector: 'jhi-dvd-detail',
  templateUrl: './dvd-detail.component.html',
})
export class DvdDetailComponent implements OnInit {
  dvd: IDvd | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dvd }) => {
      this.dvd = dvd;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
