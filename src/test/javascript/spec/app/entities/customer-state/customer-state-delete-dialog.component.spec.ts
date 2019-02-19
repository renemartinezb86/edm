/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EdmTestModule } from '../../../test.module';
import { CustomerStateDeleteDialogComponent } from 'app/entities/customer-state/customer-state-delete-dialog.component';
import { CustomerStateService } from 'app/entities/customer-state/customer-state.service';

describe('Component Tests', () => {
    describe('CustomerState Management Delete Component', () => {
        let comp: CustomerStateDeleteDialogComponent;
        let fixture: ComponentFixture<CustomerStateDeleteDialogComponent>;
        let service: CustomerStateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [CustomerStateDeleteDialogComponent]
            })
                .overrideTemplate(CustomerStateDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CustomerStateDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomerStateService);
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
