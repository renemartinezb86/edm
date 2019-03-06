import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'discount-process',
                loadChildren: './discount-process/discount-process.module#EdmDiscountProcessModule'
            },
            {
                path: 'customer-state',
                loadChildren: './customer-state/customer-state.module#EdmCustomerStateModule'
            },
            {
                path: 'plan-discount',
                loadChildren: './plan-discount/plan-discount.module#EdmPlanDiscountModule'
            },
            {
                path: 'environment',
                loadChildren: './environment/environment.module#EdmEnvironmentModule'
            },
            {
                path: 'discount-process',
                loadChildren: './discount-process/discount-process.module#EdmDiscountProcessModule'
            },
            {
                path: 'customer-state',
                loadChildren: './customer-state/customer-state.module#EdmCustomerStateModule'
            },
            {
                path: 'plan-discount',
                loadChildren: './plan-discount/plan-discount.module#EdmPlanDiscountModule'
            },
            {
                path: 'environment',
                loadChildren: './environment/environment.module#EdmEnvironmentModule'
            },
            {
                path: 'discount-process',
                loadChildren: './discount-process/discount-process.module#EdmDiscountProcessModule'
            },
            {
                path: 'customer-state',
                loadChildren: './customer-state/customer-state.module#EdmCustomerStateModule'
            },
            {
                path: 'plan-discount',
                loadChildren: './plan-discount/plan-discount.module#EdmPlanDiscountModule'
            },
            {
                path: 'environment',
                loadChildren: './environment/environment.module#EdmEnvironmentModule'
            },
            {
                path: 'discount-process',
                loadChildren: './discount-process/discount-process.module#EdmDiscountProcessModule'
            },
            {
                path: 'customer-state',
                loadChildren: './customer-state/customer-state.module#EdmCustomerStateModule'
            },
            {
                path: 'plan-discount',
                loadChildren: './plan-discount/plan-discount.module#EdmPlanDiscountModule'
            },
            {
                path: 'environment',
                loadChildren: './environment/environment.module#EdmEnvironmentModule'
            },
            {
                path: 'discount-process',
                loadChildren: './discount-process/discount-process.module#EdmDiscountProcessModule'
            },
            {
                path: 'customer-state',
                loadChildren: './customer-state/customer-state.module#EdmCustomerStateModule'
            },
            {
                path: 'plan-discount',
                loadChildren: './plan-discount/plan-discount.module#EdmPlanDiscountModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EdmEntityModule {}
