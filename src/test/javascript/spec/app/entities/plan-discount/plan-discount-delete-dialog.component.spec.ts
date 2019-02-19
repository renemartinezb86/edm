/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EdmTestModule } from '../../../test.module';
import { PlanDiscountDeleteDialogComponent } from 'app/entities/plan-discount/plan-discount-delete-dialog.component';
import { PlanDiscountService } from 'app/entities/plan-discount/plan-discount.service';

describe('Component Tests', () => {
    describe('PlanDiscount Management Delete Component', () => {
        let comp: PlanDiscountDeleteDialogComponent;
        let fixture: ComponentFixture<PlanDiscountDeleteDialogComponent>;
        let service: PlanDiscountService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [PlanDiscountDeleteDialogComponent]
            })
                .overrideTemplate(PlanDiscountDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanDiscountDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanDiscountService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete('123');
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith('123');
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
