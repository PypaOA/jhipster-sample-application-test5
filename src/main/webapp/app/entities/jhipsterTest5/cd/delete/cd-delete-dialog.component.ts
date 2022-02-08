import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICd } from '../cd.model';
import { CdService } from '../service/cd.service';

@Component({
  templateUrl: './cd-delete-dialog.component.html',
})
export class CdDeleteDialogComponent {
  cd?: ICd;

  constructor(protected cdService: CdService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cdService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
