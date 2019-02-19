import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EdmSharedModule } from 'app/shared';
import {
    EnvironmentComponent,
    EnvironmentDetailComponent,
    EnvironmentUpdateComponent,
    EnvironmentDeletePopupComponent,
    EnvironmentDeleteDialogComponent,
    environmentRoute,
    environmentPopupRoute
} from './';

const ENTITY_STATES = [...environmentRoute, ...environmentPopupRoute];

@NgModule({
    imports: [EdmSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EnvironmentComponent,
        EnvironmentDetailComponent,
        EnvironmentUpdateComponent,
        EnvironmentDeleteDialogComponent,
        EnvironmentDeletePopupComponent
    ],
    entryComponents: [EnvironmentComponent, EnvironmentUpdateComponent, EnvironmentDeleteDialogComponent, EnvironmentDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EdmEnvironmentModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
