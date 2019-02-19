import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnvironment } from 'app/shared/model/environment.model';

@Component({
    selector: 'jhi-environment-detail',
    templateUrl: './environment-detail.component.html'
})
export class EnvironmentDetailComponent implements OnInit {
    environment: IEnvironment;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ environment }) => {
            this.environment = environment;
        });
    }

    previousState() {
        window.history.back();
    }
}
