import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DiscountProcess } from 'app/shared/model/discount-process.model';
import { DiscountProcessService } from './discount-process.service';
import { DiscountProcessComponent } from './discount-process.component';
import { DiscountProcessDetailComponent } from './discount-process-detail.component';
import { DiscountProcessUpdateComponent } from './discount-process-update.component';
import { DiscountProcessDeletePopupComponent } from './discount-process-delete-dialog.component';
import { IDiscountProcess } from 'app/shared/model/discount-process.model';

@Injectable({ providedIn: 'root' })
export class DiscountProcessResolve implements Resolve<IDiscountProcess> {
    constructor(private service: DiscountProcessService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDiscountProcess> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DiscountProcess>) => response.ok),
                map((discountProcess: HttpResponse<DiscountProcess>) => discountProcess.body)
            );
        }
        return of(new DiscountProcess());
    }
}

export const discountProcessRoute: Routes = [
    {
        path: '',
        component: DiscountProcessComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'edmApp.discountProcess.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DiscountProcessDetailComponent,
        resolve: {
            discountProcess: DiscountProcessResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.discountProcess.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DiscountProcessUpdateComponent,
        resolve: {
            discountProcess: DiscountProcessResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.discountProcess.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DiscountProcessUpdateComponent,
        resolve: {
            discountProcess: DiscountProcessResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.discountProcess.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const discountProcessPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DiscountProcessDeletePopupComponent,
        resolve: {
            discountProcess: DiscountProcessResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.discountProcess.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
