import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StockValueComponent } from './stock-value.component';
import { StockValueDetailComponent } from './stock-value-detail.component';
import { StockValuePopupComponent } from './stock-value-dialog.component';
import { StockValueDeletePopupComponent } from './stock-value-delete-dialog.component';

export const stockValueRoute: Routes = [
    {
        path: 'stock-value',
        component: StockValueComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockValues'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'stock-value/:id',
        component: StockValueDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockValues'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stockValuePopupRoute: Routes = [
    {
        path: 'stock-value-new',
        component: StockValuePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockValues'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stock-value/:id/edit',
        component: StockValuePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockValues'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stock-value/:id/delete',
        component: StockValueDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockValues'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
