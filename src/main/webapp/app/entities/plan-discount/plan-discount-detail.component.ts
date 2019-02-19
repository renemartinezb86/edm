import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanDiscount } from 'app/shared/model/plan-discount.model';

@Component({
    selector: 'jhi-plan-discount-detail',
    templateUrl: './plan-discount-detail.component.html'
})
export class PlanDiscountDetailComponent implements OnInit {
    planDiscount: IPlanDiscount;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planDiscount }) => {
            this.planDiscount = planDiscount;
        });
    }

    previousState() {
        window.history.back();
    }
}
