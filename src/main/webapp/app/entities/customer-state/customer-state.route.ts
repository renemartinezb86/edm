import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerState } from 'app/shared/model/customer-state.model';
import { CustomerStateService } from './customer-state.service';
import { CustomerStateComponent } from './customer-state.component';
import { CustomerStateDetailComponent } from './customer-state-detail.component';
import { CustomerStateUpdateComponent } from './customer-state-update.component';
import { CustomerStateDeletePopupComponent } from './customer-state-delete-dialog.component';
import { ICustomerState } from 'app/shared/model/customer-state.model';

@Injectable({ providedIn: 'root' })
export class CustomerStateResolve implements Resolve<ICustomerState> {
    constructor(private service: CustomerStateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomerState> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CustomerState>) => response.ok),
                map((customerState: HttpResponse<CustomerState>) => customerState.body)
            );
        }
        return of(new CustomerState());
    }
}

export const customerStateRoute: Routes = [
    {
        path: '',
        component: CustomerStateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'edmApp.customerState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CustomerStateDetailComponent,
        resolve: {
            customerState: CustomerStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.customerState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CustomerStateUpdateComponent,
        resolve: {
            customerState: CustomerStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.customerState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CustomerStateUpdateComponent,
        resolve: {
            customerState: CustomerStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.customerState.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const customerStatePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CustomerStateDeletePopupComponent,
        resolve: {
            customerState: CustomerStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.customerState.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
