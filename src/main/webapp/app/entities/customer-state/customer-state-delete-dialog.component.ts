import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerState } from 'app/shared/model/customer-state.model';
import { CustomerStateService } from './customer-state.service';

@Component({
    selector: 'jhi-customer-state-delete-dialog',
    templateUrl: './customer-state-delete-dialog.component.html'
})
export class CustomerStateDeleteDialogComponent {
    customerState: ICustomerState;

    constructor(
        protected customerStateService: CustomerStateService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.customerStateService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'customerStateListModification',
                content: 'Deleted an customerState'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-customer-state-delete-popup',
    template: ''
})
export class CustomerStateDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerState }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CustomerStateDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.customerState = customerState;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/customer-state', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/customer-state', { outlets: { popup: null } }]);
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
