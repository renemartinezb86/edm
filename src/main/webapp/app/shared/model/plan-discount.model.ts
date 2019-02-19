export interface IPlanDiscount {
    id?: string;
    name?: string;
    position?: number;
    discountPercentage?: number;
    active?: boolean;
}

export class PlanDiscount implements IPlanDiscount {
    constructor(
        public id?: string,
        public name?: string,
        public position?: number,
        public discountPercentage?: number,
        public active?: boolean
    ) {
        this.active = this.active || false;
    }
}
