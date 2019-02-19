import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDiscountProcess } from 'app/shared/model/discount-process.model';

@Component({
    selector: 'jhi-discount-process-detail',
    templateUrl: './discount-process-detail.component.html'
})
export class DiscountProcessDetailComponent implements OnInit {
    discountProcess: IDiscountProcess;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ discountProcess }) => {
            this.discountProcess = discountProcess;
        });
    }

    previousState() {
        window.history.back();
    }
}
