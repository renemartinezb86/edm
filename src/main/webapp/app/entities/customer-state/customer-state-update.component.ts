import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ICustomerState } from 'app/shared/model/customer-state.model';
import { CustomerStateService } from './customer-state.service';

@Component({
    selector: 'jhi-customer-state-update',
    templateUrl: './customer-state-update.component.html'
})
export class CustomerStateUpdateComponent implements OnInit {
    customerState: ICustomerState;
    isSaving: boolean;

    constructor(protected customerStateService: CustomerStateService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ customerState }) => {
            this.customerState = customerState;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.customerState.id !== undefined) {
            this.subscribeToSaveResponse(this.customerStateService.update(this.customerState));
        } else {
            this.subscribeToSaveResponse(this.customerStateService.create(this.customerState));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerState>>) {
        result.subscribe((res: HttpResponse<ICustomerState>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
