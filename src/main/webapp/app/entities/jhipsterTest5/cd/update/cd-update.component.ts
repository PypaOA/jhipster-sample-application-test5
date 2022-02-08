import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICd, Cd } from '../cd.model';
import { CdService } from '../service/cd.service';
import { State } from 'app/entities/enumerations/state.model';

@Component({
  selector: 'jhi-cd-update',
  templateUrl: './cd-update.component.html',
})
export class CdUpdateComponent implements OnInit {
  isSaving = false;
  stateValues = Object.keys(State);

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    performer: [],
    releaseYear: [],
    discCount: [],
    medium: [],
    label: [],
    state: [],
    added: [],
  });

  constructor(protected cdService: CdService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cd }) => {
      if (cd.id === undefined) {
        const today = dayjs().startOf('day');
        cd.added = today;
      }

      this.updateForm(cd);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cd = this.createFromForm();
    if (cd.id !== undefined) {
      this.subscribeToSaveResponse(this.cdService.update(cd));
    } else {
      this.subscribeToSaveResponse(this.cdService.create(cd));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICd>>): void {
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

  protected updateForm(cd: ICd): void {
    this.editForm.patchValue({
      id: cd.id,
      name: cd.name,
      performer: cd.performer,
      releaseYear: cd.releaseYear,
      discCount: cd.discCount,
      medium: cd.medium,
      label: cd.label,
      state: cd.state,
      added: cd.added ? cd.added.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ICd {
    return {
      ...new Cd(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      performer: this.editForm.get(['performer'])!.value,
      releaseYear: this.editForm.get(['releaseYear'])!.value,
      discCount: this.editForm.get(['discCount'])!.value,
      medium: this.editForm.get(['medium'])!.value,
      label: this.editForm.get(['label'])!.value,
      state: this.editForm.get(['state'])!.value,
      added: this.editForm.get(['added'])!.value ? dayjs(this.editForm.get(['added'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
