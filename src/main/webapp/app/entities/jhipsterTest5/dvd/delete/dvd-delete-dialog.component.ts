import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDvd } from '../dvd.model';
import { DvdService } from '../service/dvd.service';

@Component({
  templateUrl: './dvd-delete-dialog.component.html',
})
export class DvdDeleteDialogComponent {
  dvd?: IDvd;

  constructor(protected dvdService: DvdService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dvdService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
