/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { CustomerStateUpdateComponent } from 'app/entities/customer-state/customer-state-update.component';
import { CustomerStateService } from 'app/entities/customer-state/customer-state.service';
import { CustomerState } from 'app/shared/model/customer-state.model';

describe('Component Tests', () => {
    describe('CustomerState Management Update Component', () => {
        let comp: CustomerStateUpdateComponent;
        let fixture: ComponentFixture<CustomerStateUpdateComponent>;
        let service: CustomerStateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [CustomerStateUpdateComponent]
            })
                .overrideTemplate(CustomerStateUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CustomerStateUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomerStateService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CustomerState('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.customerState = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CustomerState();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.customerState = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
