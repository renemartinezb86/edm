import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerState } from 'app/shared/model/customer-state.model';

@Component({
    selector: 'jhi-customer-state-detail',
    templateUrl: './customer-state-detail.component.html'
})
export class CustomerStateDetailComponent implements OnInit {
    customerState: ICustomerState;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerState }) => {
            this.customerState = customerState;
        });
    }

    previousState() {
        window.history.back();
    }
}
