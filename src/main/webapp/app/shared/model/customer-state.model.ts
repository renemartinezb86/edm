export interface ICustomerState {
    id?: string;
    rut?: string;
    blackList?: boolean;
    whiteList?: boolean;
}

export class CustomerState implements ICustomerState {
    constructor(public id?: string, public rut?: string, public blackList?: boolean, public whiteList?: boolean) {
        this.blackList = this.blackList || false;
        this.whiteList = this.whiteList || false;
    }
}
