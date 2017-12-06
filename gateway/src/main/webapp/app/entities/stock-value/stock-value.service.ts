import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { StockValue } from './stock-value.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StockValueService {

    private resourceUrl = '/market/api/stock-values';

    constructor(private http: Http) { }

    create(stockValue: StockValue): Observable<StockValue> {
        const copy = this.convert(stockValue);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(stockValue: StockValue): Observable<StockValue> {
        const copy = this.convert(stockValue);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<StockValue> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to StockValue.
     */
    private convertItemFromServer(json: any): StockValue {
        const entity: StockValue = Object.assign(new StockValue(), json);
        return entity;
    }

    /**
     * Convert a StockValue to a JSON which can be sent to the server.
     */
    private convert(stockValue: StockValue): StockValue {
        const copy: StockValue = Object.assign({}, stockValue);
        return copy;
    }
}
