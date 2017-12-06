import { BaseEntity } from './../../shared';

export class StockValue implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public amount?: number,
    ) {
    }
}
