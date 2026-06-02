import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotificationSettings } from './notification-settings.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationSettingsService {
  private readonly apiUrl = '/api/notification-settings';

  constructor(private readonly http: HttpClient) {}

  getNotificationSettings(): Observable<NotificationSettings> {
    return this.http.get<NotificationSettings>(this.apiUrl);
  }

  updateNotificationSettings(settings: NotificationSettings): Observable<void> {
    return this.http.put<void>(this.apiUrl, settings);
  }
}
