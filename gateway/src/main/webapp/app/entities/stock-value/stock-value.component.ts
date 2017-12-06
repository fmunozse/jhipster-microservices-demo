import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { StockValue } from './stock-value.model';
import { StockValueService } from './stock-value.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-stock-value',
    templateUrl: './stock-value.component.html'
})
export class StockValueComponent implements OnInit, OnDestroy {
stockValues: StockValue[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private stockValueService: StockValueService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.stockValueService.query().subscribe(
            (res: ResponseWrapper) => {
                this.stockValues = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInStockValues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: StockValue) {
        return item.id;
    }
    registerChangeInStockValues() {
        this.eventSubscriber = this.eventManager.subscribe('stockValueListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
