import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchCriteria } from './search-criteria.model';

@Injectable({
  providedIn: 'root'
})
export class SearchCriteriaService {
  private readonly basePath = '/api/search-criteria';

  constructor(private readonly http: HttpClient) { }

  getSearchCriteria(): Observable<SearchCriteria> {
    return this.http.get<SearchCriteria>(this.basePath);
  }

  updateSearchCriteria(criteria: SearchCriteria): Observable<void> {
    return this.http.put<void>(this.basePath, criteria);
  }
}
