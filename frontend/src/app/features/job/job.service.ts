import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Job, JobStatus, Page } from './job.model';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private readonly basePath = '/api/jobs';

  constructor(private readonly http: HttpClient) { }

  getJobs(page: number, pageSize: number, sort: string): Observable<Page<Job>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('pageSize', pageSize.toString())
      .set('sort', sort);
    return this.http.get<Page<Job>>(this.basePath, { params });
  }

  getJob(id: number): Observable<Job> {
    return this.http.get<Job>(`${this.basePath}/${id}`);
  }

  discardJob(id: number): Observable<void> {
    return this.http.delete<void>(`${this.basePath}/${id}`);
  }

  updateJobStatus(id: number, status: JobStatus): Observable<void> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<void>(`${this.basePath}/${id}/status`, null, { params });
  }
}
