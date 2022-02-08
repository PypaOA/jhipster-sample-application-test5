import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cd',
        data: { pageTitle: 'Cds' },
        loadChildren: () => import('./jhipsterTest5/cd/cd.module').then(m => m.JhipsterTest5CdModule),
      },
      {
        path: 'dvd',
        data: { pageTitle: 'Dvds' },
        loadChildren: () => import('./jhipsterTest5/dvd/dvd.module').then(m => m.JhipsterTest5DvdModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
