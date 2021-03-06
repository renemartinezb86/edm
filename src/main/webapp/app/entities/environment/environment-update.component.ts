import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IEnvironment } from 'app/shared/model/environment.model';
import { EnvironmentService } from './environment.service';

@Component({
    selector: 'jhi-environment-update',
    templateUrl: './environment-update.component.html'
})
export class EnvironmentUpdateComponent implements OnInit {
    environment: IEnvironment;
    isSaving: boolean;

    constructor(protected environmentService: EnvironmentService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ environment }) => {
            this.environment = environment;
        });
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
}
