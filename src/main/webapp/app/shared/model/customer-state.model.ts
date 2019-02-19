export interface ICustomerState {
    id?: string;
    rut?: string;
    active?: boolean;
    blackList?: boolean;
    whiteList?: boolean;
}

export class CustomerState implements ICustomerState {
    constructor(public id?: string, public rut?: string, public active?: boolean, public blackList?: boolean, public whiteList?: boolean) {
        this.active = this.active || false;
        this.blackList = this.blackList || false;
        this.whiteList = this.whiteList || false;
    }
}
