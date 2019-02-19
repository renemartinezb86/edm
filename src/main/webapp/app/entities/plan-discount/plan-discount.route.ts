import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlanDiscount } from 'app/shared/model/plan-discount.model';
import { PlanDiscountService } from './plan-discount.service';
import { PlanDiscountComponent } from './plan-discount.component';
import { PlanDiscountDetailComponent } from './plan-discount-detail.component';
import { PlanDiscountUpdateComponent } from './plan-discount-update.component';
import { PlanDiscountDeletePopupComponent } from './plan-discount-delete-dialog.component';
import { IPlanDiscount } from 'app/shared/model/plan-discount.model';

@Injectable({ providedIn: 'root' })
export class PlanDiscountResolve implements Resolve<IPlanDiscount> {
    constructor(private service: PlanDiscountService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlanDiscount> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PlanDiscount>) => response.ok),
                map((planDiscount: HttpResponse<PlanDiscount>) => planDiscount.body)
            );
        }
        return of(new PlanDiscount());
    }
}

export const planDiscountRoute: Routes = [
    {
        path: '',
        component: PlanDiscountComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'edmApp.planDiscount.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PlanDiscountDetailComponent,
        resolve: {
            planDiscount: PlanDiscountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.planDiscount.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PlanDiscountUpdateComponent,
        resolve: {
            planDiscount: PlanDiscountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.planDiscount.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PlanDiscountUpdateComponent,
        resolve: {
            planDiscount: PlanDiscountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.planDiscount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planDiscountPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PlanDiscountDeletePopupComponent,
        resolve: {
            planDiscount: PlanDiscountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.planDiscount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
