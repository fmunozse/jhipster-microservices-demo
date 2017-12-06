import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { StockValue } from './stock-value.model';
import { StockValuePopupService } from './stock-value-popup.service';
import { StockValueService } from './stock-value.service';

@Component({
    selector: 'jhi-stock-value-delete-dialog',
    templateUrl: './stock-value-delete-dialog.component.html'
})
export class StockValueDeleteDialogComponent {

    stockValue: StockValue;

    constructor(
        private stockValueService: StockValueService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stockValueService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'stockValueListModification',
                content: 'Deleted an stockValue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-stock-value-delete-popup',
    template: ''
})
export class StockValueDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stockValuePopupService: StockValuePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.stockValuePopupService
                .open(StockValueDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
