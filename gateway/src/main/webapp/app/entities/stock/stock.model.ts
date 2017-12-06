import { BaseEntity } from './../../shared';

export class Stock implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public quantity?: number,
        public userId?: string,
        public amount?: number
    ) {
    }
}
