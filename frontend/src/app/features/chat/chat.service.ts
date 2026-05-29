import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ChatMessage} from './chat.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private readonly basePath = '/api/jobs';

  constructor(private readonly http: HttpClient) { }

  getChatHistory(jobId: number): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.basePath}/${jobId}/chat/messages`);
  }

  sendMessage(jobId: number, message: string): Observable<ChatMessage> {
    return this.http.post<ChatMessage>(`${this.basePath}/${jobId}/chat/message`, { message });
  }

}
