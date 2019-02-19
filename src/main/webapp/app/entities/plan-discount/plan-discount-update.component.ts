import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IPlanDiscount } from 'app/shared/model/plan-discount.model';
import { PlanDiscountService } from './plan-discount.service';

@Component({
    selector: 'jhi-plan-discount-update',
    templateUrl: './plan-discount-update.component.html'
})
export class PlanDiscountUpdateComponent implements OnInit {
    planDiscount: IPlanDiscount;
    isSaving: boolean;

    constructor(protected planDiscountService: PlanDiscountService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planDiscount }) => {
            this.planDiscount = planDiscount;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.planDiscount.id !== undefined) {
            this.subscribeToSaveResponse(this.planDiscountService.update(this.planDiscount));
        } else {
            this.subscribeToSaveResponse(this.planDiscountService.create(this.planDiscount));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanDiscount>>) {
        result.subscribe((res: HttpResponse<IPlanDiscount>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
