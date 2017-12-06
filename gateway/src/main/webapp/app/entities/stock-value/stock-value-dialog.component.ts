import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { StockValue } from './stock-value.model';
import { StockValuePopupService } from './stock-value-popup.service';
import { StockValueService } from './stock-value.service';

@Component({
    selector: 'jhi-stock-value-dialog',
    templateUrl: './stock-value-dialog.component.html'
})
export class StockValueDialogComponent implements OnInit {

    stockValue: StockValue;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private stockValueService: StockValueService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.stockValue.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stockValueService.update(this.stockValue));
        } else {
            this.subscribeToSaveResponse(
                this.stockValueService.create(this.stockValue));
        }
    }

    private subscribeToSaveResponse(result: Observable<StockValue>) {
        result.subscribe((res: StockValue) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: StockValue) {
        this.eventManager.broadcast({ name: 'stockValueListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-stock-value-popup',
    template: ''
})
export class StockValuePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stockValuePopupService: StockValuePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.stockValuePopupService
                    .open(StockValueDialogComponent as Component, params['id']);
            } else {
                this.stockValuePopupService
                    .open(StockValueDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
