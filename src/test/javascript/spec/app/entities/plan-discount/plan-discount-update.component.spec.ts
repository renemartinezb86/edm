/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { PlanDiscountUpdateComponent } from 'app/entities/plan-discount/plan-discount-update.component';
import { PlanDiscountService } from 'app/entities/plan-discount/plan-discount.service';
import { PlanDiscount } from 'app/shared/model/plan-discount.model';

describe('Component Tests', () => {
    describe('PlanDiscount Management Update Component', () => {
        let comp: PlanDiscountUpdateComponent;
        let fixture: ComponentFixture<PlanDiscountUpdateComponent>;
        let service: PlanDiscountService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [PlanDiscountUpdateComponent]
            })
                .overrideTemplate(PlanDiscountUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanDiscountUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanDiscountService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PlanDiscount('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.planDiscount = entity;
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
                    const entity = new PlanDiscount();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.planDiscount = entity;
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
