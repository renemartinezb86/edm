/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { DiscountProcessUpdateComponent } from 'app/entities/discount-process/discount-process-update.component';
import { DiscountProcessService } from 'app/entities/discount-process/discount-process.service';
import { DiscountProcess } from 'app/shared/model/discount-process.model';

describe('Component Tests', () => {
    describe('DiscountProcess Management Update Component', () => {
        let comp: DiscountProcessUpdateComponent;
        let fixture: ComponentFixture<DiscountProcessUpdateComponent>;
        let service: DiscountProcessService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [DiscountProcessUpdateComponent]
            })
                .overrideTemplate(DiscountProcessUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DiscountProcessUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DiscountProcessService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DiscountProcess('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.discountProcess = entity;
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
                    const entity = new DiscountProcess();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.discountProcess = entity;
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
