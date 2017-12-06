import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from '../../shared';
import {
    StockValueService,
    StockValuePopupService,
    StockValueComponent,
    StockValueDetailComponent,
    StockValueDialogComponent,
    StockValuePopupComponent,
    StockValueDeletePopupComponent,
    StockValueDeleteDialogComponent,
    stockValueRoute,
    stockValuePopupRoute,
} from './';

const ENTITY_STATES = [
    ...stockValueRoute,
    ...stockValuePopupRoute,
];

@NgModule({
    imports: [
        GatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StockValueComponent,
        StockValueDetailComponent,
        StockValueDialogComponent,
        StockValueDeleteDialogComponent,
        StockValuePopupComponent,
        StockValueDeletePopupComponent,
    ],
    entryComponents: [
        StockValueComponent,
        StockValueDialogComponent,
        StockValuePopupComponent,
        StockValueDeleteDialogComponent,
        StockValueDeletePopupComponent,
    ],
    providers: [
        StockValueService,
        StockValuePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayStockValueModule {}
