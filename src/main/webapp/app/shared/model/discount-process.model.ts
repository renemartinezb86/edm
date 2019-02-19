import { Moment } from 'moment';

export interface IDiscountProcess {
    id?: string;
    quantity?: number;
    dateToProcess?: Moment;
    createdDate?: Moment;
    sqlFilePath?: string;
}

export class DiscountProcess implements IDiscountProcess {
    constructor(
        public id?: string,
        public quantity?: number,
        public dateToProcess?: Moment,
        public createdDate?: Moment,
        public sqlFilePath?: string
    ) {}
}
