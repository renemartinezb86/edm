import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IEnvironment } from 'app/shared/model/environment.model';
import { EnvironmentService } from './environment.service';
import { IDiscountProcess } from 'app/shared/model/discount-process.model';
import { DiscountProcessService } from 'app/entities/discount-process';

@Component({
    selector: 'jhi-environment-update',
    templateUrl: './environment-update.component.html'
})
export class EnvironmentUpdateComponent implements OnInit {
    environment: IEnvironment;
    isSaving: boolean;

    discountprocesses: IDiscountProcess[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected environmentService: EnvironmentService,
        protected discountProcessService: DiscountProcessService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ environment }) => {
            this.environment = environment;
        });
        this.discountProcessService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDiscountProcess[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDiscountProcess[]>) => response.body)
            )
            .subscribe((res: IDiscountProcess[]) => (this.discountprocesses = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.environment.id !== undefined) {
            this.subscribeToSaveResponse(this.environmentService.update(this.environment));
        } else {
            this.subscribeToSaveResponse(this.environmentService.create(this.environment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnvironment>>) {
        result.subscribe((res: HttpResponse<IEnvironment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDiscountProcessById(index: number, item: IDiscountProcess) {
        return item.id;
    }
}
