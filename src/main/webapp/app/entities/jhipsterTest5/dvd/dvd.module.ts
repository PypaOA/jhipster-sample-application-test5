import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DvdComponent } from './list/dvd.component';
import { DvdDetailComponent } from './detail/dvd-detail.component';
import { DvdUpdateComponent } from './update/dvd-update.component';
import { DvdDeleteDialogComponent } from './delete/dvd-delete-dialog.component';
import { DvdRoutingModule } from './route/dvd-routing.module';

@NgModule({
  imports: [SharedModule, DvdRoutingModule],
  declarations: [DvdComponent, DvdDetailComponent, DvdUpdateComponent, DvdDeleteDialogComponent],
  entryComponents: [DvdDeleteDialogComponent],
})
export class JhipsterTest5DvdModule {}
