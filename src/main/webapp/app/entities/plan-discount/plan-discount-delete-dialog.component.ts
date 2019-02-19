import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanDiscount } from 'app/shared/model/plan-discount.model';
import { PlanDiscountService } from './plan-discount.service';

@Component({
    selector: 'jhi-plan-discount-delete-dialog',
    templateUrl: './plan-discount-delete-dialog.component.html'
})
export class PlanDiscountDeleteDialogComponent {
    planDiscount: IPlanDiscount;

    constructor(
        protected planDiscountService: PlanDiscountService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.planDiscountService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'planDiscountListModification',
                content: 'Deleted an planDiscount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-plan-discount-delete-popup',
    template: ''
})
export class PlanDiscountDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planDiscount }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlanDiscountDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.planDiscount = planDiscount;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/plan-discount', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/plan-discount', { outlets: { popup: null } }]);
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
