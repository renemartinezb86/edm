/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { PlanDiscountDetailComponent } from 'app/entities/plan-discount/plan-discount-detail.component';
import { PlanDiscount } from 'app/shared/model/plan-discount.model';

describe('Component Tests', () => {
    describe('PlanDiscount Management Detail Component', () => {
        let comp: PlanDiscountDetailComponent;
        let fixture: ComponentFixture<PlanDiscountDetailComponent>;
        const route = ({ data: of({ planDiscount: new PlanDiscount('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [PlanDiscountDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanDiscountDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanDiscountDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planDiscount).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
