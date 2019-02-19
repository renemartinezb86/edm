/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EdmTestModule } from '../../../test.module';
import { EnvironmentUpdateComponent } from 'app/entities/environment/environment-update.component';
import { EnvironmentService } from 'app/entities/environment/environment.service';
import { Environment } from 'app/shared/model/environment.model';

describe('Component Tests', () => {
    describe('Environment Management Update Component', () => {
        let comp: EnvironmentUpdateComponent;
        let fixture: ComponentFixture<EnvironmentUpdateComponent>;
        let service: EnvironmentService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EdmTestModule],
                declarations: [EnvironmentUpdateComponent]
            })
                .overrideTemplate(EnvironmentUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EnvironmentUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EnvironmentService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Environment('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.environment = entity;
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
                    const entity = new Environment();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.environment = entity;
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
