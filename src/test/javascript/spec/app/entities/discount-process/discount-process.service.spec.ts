/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DiscountProcessService } from 'app/entities/discount-process/discount-process.service';
import { IDiscountProcess, DiscountProcess } from 'app/shared/model/discount-process.model';

describe('Service Tests', () => {
    describe('DiscountProcess Service', () => {
        let injector: TestBed;
        let service: DiscountProcessService;
        let httpMock: HttpTestingController;
        let elemDefault: IDiscountProcess;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(DiscountProcessService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new DiscountProcess('ID', 0, currentDate, currentDate, 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateToProcess: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a DiscountProcess', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        dateToProcess: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateToProcess: currentDate,
                        createdDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new DiscountProcess(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a DiscountProcess', async () => {
                const returnedFromService = Object.assign(
                    {
                        quantity: 1,
                        dateToProcess: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        sqlFilePath: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dateToProcess: currentDate,
                        createdDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of DiscountProcess', async () => {
                const returnedFromService = Object.assign(
                    {
                        quantity: 1,
                        dateToProcess: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        sqlFilePath: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateToProcess: currentDate,
                        createdDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a DiscountProcess', async () => {
                const rxPromise = service.delete('123').subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
