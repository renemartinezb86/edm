import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEnvironment } from 'app/shared/model/environment.model';

type EntityResponseType = HttpResponse<IEnvironment>;
type EntityArrayResponseType = HttpResponse<IEnvironment[]>;

@Injectable({ providedIn: 'root' })
export class EnvironmentService {
    public resourceUrl = SERVER_API_URL + 'api/environments';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/environments';

    constructor(protected http: HttpClient) {}

    create(environment: IEnvironment): Observable<EntityResponseType> {
        return this.http.post<IEnvironment>(this.resourceUrl, environment, { observe: 'response' });
    }

    update(environment: IEnvironment): Observable<EntityResponseType> {
        return this.http.put<IEnvironment>(this.resourceUrl, environment, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IEnvironment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEnvironment[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEnvironment[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
