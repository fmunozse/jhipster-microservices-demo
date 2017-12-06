/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StockValueDetailComponent } from '../../../../../../main/webapp/app/entities/stock-value/stock-value-detail.component';
import { StockValueService } from '../../../../../../main/webapp/app/entities/stock-value/stock-value.service';
import { StockValue } from '../../../../../../main/webapp/app/entities/stock-value/stock-value.model';

describe('Component Tests', () => {

    describe('StockValue Management Detail Component', () => {
        let comp: StockValueDetailComponent;
        let fixture: ComponentFixture<StockValueDetailComponent>;
        let service: StockValueService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayTestModule],
                declarations: [StockValueDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StockValueService,
                    JhiEventManager
                ]
            }).overrideTemplate(StockValueDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StockValueDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StockValueService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StockValue(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.stockValue).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
