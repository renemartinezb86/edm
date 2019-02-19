import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEnvironment } from 'app/shared/model/environment.model';
import { EnvironmentService } from './environment.service';

@Component({
    selector: 'jhi-environment-delete-dialog',
    templateUrl: './environment-delete-dialog.component.html'
})
export class EnvironmentDeleteDialogComponent {
    environment: IEnvironment;

    constructor(
        protected environmentService: EnvironmentService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.environmentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'environmentListModification',
                content: 'Deleted an environment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-environment-delete-popup',
    template: ''
})
export class EnvironmentDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ environment }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EnvironmentDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.environment = environment;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/environment', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/environment', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
