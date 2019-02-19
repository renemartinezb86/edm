import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerState } from 'app/shared/model/customer-state.model';

type EntityResponseType = HttpResponse<ICustomerState>;
type EntityArrayResponseType = HttpResponse<ICustomerState[]>;

@Injectable({ providedIn: 'root' })
export class CustomerStateService {
    public resourceUrl = SERVER_API_URL + 'api/customer-states';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customer-states';

    constructor(protected http: HttpClient) {}

    create(customerState: ICustomerState): Observable<EntityResponseType> {
        return this.http.post<ICustomerState>(this.resourceUrl, customerState, { observe: 'response' });
    }

    update(customerState: ICustomerState): Observable<EntityResponseType> {
        return this.http.put<ICustomerState>(this.resourceUrl, customerState, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICustomerState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomerState[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomerState[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
