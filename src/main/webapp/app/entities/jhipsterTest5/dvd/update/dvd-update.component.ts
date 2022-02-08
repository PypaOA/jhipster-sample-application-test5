import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDvd, Dvd } from '../dvd.model';
import { DvdService } from '../service/dvd.service';
import { State } from 'app/entities/enumerations/state.model';

@Component({
  selector: 'jhi-dvd-update',
  templateUrl: './dvd-update.component.html',
})
export class DvdUpdateComponent implements OnInit {
  isSaving = false;
  stateValues = Object.keys(State);

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    performer: [],
    releaseYear: [],
    discCount: [],
    format: [],
    lang: [],
    state: [],
    added: [],
  });

  constructor(protected dvdService: DvdService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dvd }) => {
      if (dvd.id === undefined) {
        const today = dayjs().startOf('day');
        dvd.added = today;
      }

      this.updateForm(dvd);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dvd = this.createFromForm();
    if (dvd.id !== undefined) {
      this.subscribeToSaveResponse(this.dvdService.update(dvd));
    } else {
      this.subscribeToSaveResponse(this.dvdService.create(dvd));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDvd>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(dvd: IDvd): void {
    this.editForm.patchValue({
      id: dvd.id,
      name: dvd.name,
      performer: dvd.performer,
      releaseYear: dvd.releaseYear,
      discCount: dvd.discCount,
      format: dvd.format,
      lang: dvd.lang,
      state: dvd.state,
      added: dvd.added ? dvd.added.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IDvd {
    return {
      ...new Dvd(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      performer: this.editForm.get(['performer'])!.value,
      releaseYear: this.editForm.get(['releaseYear'])!.value,
      discCount: this.editForm.get(['discCount'])!.value,
      format: this.editForm.get(['format'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      state: this.editForm.get(['state'])!.value,
      added: this.editForm.get(['added'])!.value ? dayjs(this.editForm.get(['added'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
