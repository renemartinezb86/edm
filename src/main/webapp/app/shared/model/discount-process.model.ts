import { Moment } from 'moment';
import { IEnvironment } from 'app/shared/model/environment.model';

export interface IDiscountProcess {
    id?: string;
    quantity?: number;
    dateToProcess?: Moment;
    createdDate?: Moment;
    sqlFilePath?: string;
    environment?: IEnvironment;
}

export class DiscountProcess implements IDiscountProcess {
    constructor(
        public id?: string,
        public quantity?: number,
        public dateToProcess?: Moment,
        public createdDate?: Moment,
        public sqlFilePath?: string,
        public environment?: IEnvironment
    ) {}
}
