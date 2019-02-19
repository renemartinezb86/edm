import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EdmSharedModule } from 'app/shared';
import {
    DiscountProcessComponent,
    DiscountProcessDetailComponent,
    DiscountProcessUpdateComponent,
    DiscountProcessDeletePopupComponent,
    DiscountProcessDeleteDialogComponent,
    discountProcessRoute,
    discountProcessPopupRoute
} from './';

const ENTITY_STATES = [...discountProcessRoute, ...discountProcessPopupRoute];

@NgModule({
    imports: [EdmSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DiscountProcessComponent,
        DiscountProcessDetailComponent,
        DiscountProcessUpdateComponent,
        DiscountProcessDeleteDialogComponent,
        DiscountProcessDeletePopupComponent
    ],
    entryComponents: [
        DiscountProcessComponent,
        DiscountProcessUpdateComponent,
        DiscountProcessDeleteDialogComponent,
        DiscountProcessDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EdmDiscountProcessModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
