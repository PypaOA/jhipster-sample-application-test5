import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CdComponent } from './list/cd.component';
import { CdDetailComponent } from './detail/cd-detail.component';
import { CdUpdateComponent } from './update/cd-update.component';
import { CdDeleteDialogComponent } from './delete/cd-delete-dialog.component';
import { CdRoutingModule } from './route/cd-routing.module';

@NgModule({
  imports: [SharedModule, CdRoutingModule],
  declarations: [CdComponent, CdDetailComponent, CdUpdateComponent, CdDeleteDialogComponent],
  entryComponents: [CdDeleteDialogComponent],
})
export class JhipsterTest5CdModule {}
