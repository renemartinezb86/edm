import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDiscountProcess } from 'app/shared/model/discount-process.model';
import { DiscountProcessService } from './discount-process.service';

@Component({
    selector: 'jhi-discount-process-delete-dialog',
    templateUrl: './discount-process-delete-dialog.component.html'
})
export class DiscountProcessDeleteDialogComponent {
    discountProcess: IDiscountProcess;

    constructor(
        protected discountProcessService: DiscountProcessService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.discountProcessService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'discountProcessListModification',
                content: 'Deleted an discountProcess'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-discount-process-delete-popup',
    template: ''
})
export class DiscountProcessDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ discountProcess }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DiscountProcessDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.discountProcess = discountProcess;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/discount-process', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/discount-process', { outlets: { popup: null } }]);
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
