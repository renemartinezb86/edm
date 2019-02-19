import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EdmSharedModule } from 'app/shared';
import {
    CustomerStateComponent,
    CustomerStateDetailComponent,
    CustomerStateUpdateComponent,
    CustomerStateDeletePopupComponent,
    CustomerStateDeleteDialogComponent,
    customerStateRoute,
    customerStatePopupRoute
} from './';

const ENTITY_STATES = [...customerStateRoute, ...customerStatePopupRoute];

@NgModule({
    imports: [EdmSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CustomerStateComponent,
        CustomerStateDetailComponent,
        CustomerStateUpdateComponent,
        CustomerStateDeleteDialogComponent,
        CustomerStateDeletePopupComponent
    ],
    entryComponents: [
        CustomerStateComponent,
        CustomerStateUpdateComponent,
        CustomerStateDeleteDialogComponent,
        CustomerStateDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EdmCustomerStateModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
