/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { DiscountProcessDetailComponent } from 'app/entities/discount-process/discount-process-detail.component';
import { DiscountProcess } from 'app/shared/model/discount-process.model';

describe('Component Tests', () => {
    describe('DiscountProcess Management Detail Component', () => {
        let comp: DiscountProcessDetailComponent;
        let fixture: ComponentFixture<DiscountProcessDetailComponent>;
        const route = ({ data: of({ discountProcess: new DiscountProcess('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [DiscountProcessDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DiscountProcessDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DiscountProcessDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.discountProcess).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
