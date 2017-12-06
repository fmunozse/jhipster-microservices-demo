import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { StockValue } from './stock-value.model';
import { StockValueService } from './stock-value.service';

@Component({
    selector: 'jhi-stock-value-detail',
    templateUrl: './stock-value-detail.component.html'
})
export class StockValueDetailComponent implements OnInit, OnDestroy {

    stockValue: StockValue;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private stockValueService: StockValueService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStockValues();
    }

    load(id) {
        this.stockValueService.find(id).subscribe((stockValue) => {
            this.stockValue = stockValue;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStockValues() {
        this.eventSubscriber = this.eventManager.subscribe(
            'stockValueListModification',
            (response) => this.load(this.stockValue.id)
        );
    }
}
