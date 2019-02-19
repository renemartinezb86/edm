import { IDiscountProcess } from 'app/shared/model/discount-process.model';

export interface IEnvironment {
    id?: string;
    name?: string;
    url?: string;
    user?: string;
    pass?: string;
    discountProcesses?: IDiscountProcess[];
}

export class Environment implements IEnvironment {
    constructor(
        public id?: string,
        public name?: string,
        public url?: string,
        public user?: string,
        public pass?: string,
        public discountProcesses?: IDiscountProcess[]
    ) {}
}
