import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDiscountProcess } from 'app/shared/model/discount-process.model';

type EntityResponseType = HttpResponse<IDiscountProcess>;
type EntityArrayResponseType = HttpResponse<IDiscountProcess[]>;

@Injectable({ providedIn: 'root' })
export class DiscountProcessService {
    public resourceUrl = SERVER_API_URL + 'api/discount-processes';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/discount-processes';

    constructor(protected http: HttpClient) {}

    create(discountProcess: IDiscountProcess): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(discountProcess);
        return this.http
            .post<IDiscountProcess>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(discountProcess: IDiscountProcess): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(discountProcess);
        return this.http
            .put<IDiscountProcess>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IDiscountProcess>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDiscountProcess[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDiscountProcess[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(discountProcess: IDiscountProcess): IDiscountProcess {
        const copy: IDiscountProcess = Object.assign({}, discountProcess, {
            dateToProcess:
                discountProcess.dateToProcess != null && discountProcess.dateToProcess.isValid()
                    ? discountProcess.dateToProcess.toJSON()
                    : null,
            createdDate:
                discountProcess.createdDate != null && discountProcess.createdDate.isValid() ? discountProcess.createdDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateToProcess = res.body.dateToProcess != null ? moment(res.body.dateToProcess) : null;
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((discountProcess: IDiscountProcess) => {
                discountProcess.dateToProcess = discountProcess.dateToProcess != null ? moment(discountProcess.dateToProcess) : null;
                discountProcess.createdDate = discountProcess.createdDate != null ? moment(discountProcess.createdDate) : null;
            });
        }
        return res;
    }
}
