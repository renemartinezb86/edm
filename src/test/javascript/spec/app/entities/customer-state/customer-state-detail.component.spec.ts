/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { CustomerStateDetailComponent } from 'app/entities/customer-state/customer-state-detail.component';
import { CustomerState } from 'app/shared/model/customer-state.model';

describe('Component Tests', () => {
    describe('CustomerState Management Detail Component', () => {
        let comp: CustomerStateDetailComponent;
        let fixture: ComponentFixture<CustomerStateDetailComponent>;
        const route = ({ data: of({ customerState: new CustomerState('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [CustomerStateDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CustomerStateDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CustomerStateDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.customerState).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
