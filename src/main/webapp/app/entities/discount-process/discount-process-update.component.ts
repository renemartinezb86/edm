import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDiscountProcess } from 'app/shared/model/discount-process.model';
import { DiscountProcessService } from './discount-process.service';
import { IEnvironment } from 'app/shared/model/environment.model';
import { EnvironmentService } from 'app/entities/environment';

@Component({
    selector: 'jhi-discount-process-update',
    templateUrl: './discount-process-update.component.html'
})
export class DiscountProcessUpdateComponent implements OnInit {
    discountProcess: IDiscountProcess;
    isSaving: boolean;

    environments: IEnvironment[];
    dateToProcess: string;
    createdDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected discountProcessService: DiscountProcessService,
        protected environmentService: EnvironmentService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ discountProcess }) => {
            this.discountProcess = discountProcess;
            this.dateToProcess =
                this.discountProcess.dateToProcess != null ? this.discountProcess.dateToProcess.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.discountProcess.createdDate != null ? this.discountProcess.createdDate.format(DATE_TIME_FORMAT) : null;
        });
        this.environmentService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IEnvironment[]>) => mayBeOk.ok),
                map((response: HttpResponse<IEnvironment[]>) => response.body)
            )
            .subscribe((res: IEnvironment[]) => (this.environments = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.discountProcess.dateToProcess = this.dateToProcess != null ? moment(this.dateToProcess, DATE_FORMAT) : null;
        this.discountProcess.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        if (this.discountProcess.id !== undefined) {
            this.subscribeToSaveResponse(this.discountProcessService.update(this.discountProcess));
        } else {
            this.subscribeToSaveResponse(this.discountProcessService.create(this.discountProcess));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiscountProcess>>) {
        result.subscribe((res: HttpResponse<IDiscountProcess>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackEnvironmentById(index: number, item: IEnvironment) {
        return item.id;
    }
}
