/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EdmTestModule } from '../../../test.module';
import { EnvironmentDeleteDialogComponent } from 'app/entities/environment/environment-delete-dialog.component';
import { EnvironmentService } from 'app/entities/environment/environment.service';

describe('Component Tests', () => {
    describe('Environment Management Delete Component', () => {
        let comp: EnvironmentDeleteDialogComponent;
        let fixture: ComponentFixture<EnvironmentDeleteDialogComponent>;
        let service: EnvironmentService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [EnvironmentDeleteDialogComponent]
            })
                .overrideTemplate(EnvironmentDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EnvironmentDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EnvironmentService);
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
