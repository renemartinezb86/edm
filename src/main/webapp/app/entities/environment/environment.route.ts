import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Environment } from 'app/shared/model/environment.model';
import { EnvironmentService } from './environment.service';
import { EnvironmentComponent } from './environment.component';
import { EnvironmentDetailComponent } from './environment-detail.component';
import { EnvironmentUpdateComponent } from './environment-update.component';
import { EnvironmentDeletePopupComponent } from './environment-delete-dialog.component';
import { IEnvironment } from 'app/shared/model/environment.model';

@Injectable({ providedIn: 'root' })
export class EnvironmentResolve implements Resolve<IEnvironment> {
    constructor(private service: EnvironmentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEnvironment> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Environment>) => response.ok),
                map((environment: HttpResponse<Environment>) => environment.body)
            );
        }
        return of(new Environment());
    }
}

export const environmentRoute: Routes = [
    {
        path: '',
        component: EnvironmentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'edmApp.environment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: EnvironmentDetailComponent,
        resolve: {
            environment: EnvironmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.environment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: EnvironmentUpdateComponent,
        resolve: {
            environment: EnvironmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.environment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: EnvironmentUpdateComponent,
        resolve: {
            environment: EnvironmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.environment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const environmentPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: EnvironmentDeletePopupComponent,
        resolve: {
            environment: EnvironmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'edmApp.environment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
