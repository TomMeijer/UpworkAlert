import { Component, ElementRef, OnInit, ViewChild, signal } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Job } from '../../job/job.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {ChatMessage, ChatRole} from '../chat.model';
import {ChatService} from '../chat.service';

@Component({
  selector: 'app-job-chat-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './job-chat-modal.html',
  styleUrl: './job-chat-modal.scss'
})
export class JobChatModalComponent implements OnInit {
  job = signal<Job | undefined>(undefined);
  isLoading = signal<boolean>(false);
  messages = signal<ChatMessage[]>([]);
  newMessage = signal<string>('');
  isTyping = signal<boolean>(false);
  error = signal<string | undefined>(undefined);

  @ViewChild('scrollContainer')
  private readonly scrollContainer!: ElementRef;

  constructor(
    public bsModalRef: BsModalRef,
    private readonly chatService: ChatService
  ) {}

  set setJob(value: Job) {
    this.job.set(value);
  }

  ngOnInit(): void {
    this.loadChatHistory();
  }

  private loadChatHistory(): void {
    const currentJob = this.job();
    if (!currentJob) return;
    this.isLoading.set(true);
    this.chatService.getChatHistory(currentJob.id).subscribe({
      next: (messages) => {
        this.isLoading.set(false);
        this.messages.set(messages);
        this.scrollToBottom();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set('Failed to load chat history.');
      }
    });
  }

  sendAction(text: string): void {
    this.newMessage.set(text);
    this.sendMessage();
  }

  sendMessage(): void {
    const currentJob = this.job();
    const currentNewMessage = this.newMessage();
    if (!currentJob || !currentNewMessage.trim() || this.isTyping()) return;

    const userMessage: ChatMessage = {
      id: 0,
      role: ChatRole.USER,
      content: currentNewMessage,
      time: new Date().toISOString()
    };
    this.messages.update(m => [...m, userMessage]);
    this.scrollToBottom();

    const messageToSend = currentNewMessage;
    this.newMessage.set('');
    this.isTyping.set(true);
    this.error.set(undefined);

    this.chatService.sendMessage(currentJob.id, messageToSend).subscribe({
      next: (response) => {
        this.isTyping.set(false);
        this.messages.update(m => [...m, response]);
        this.scrollToBottom();
      },
      error: (err) => {
        this.isTyping.set(false);
        this.error.set('Failed to send message. Please try again.');
      }
    });
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      try {
        this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
      } catch (err) { }
    }, 100);
  }

  copyToClipboard(text: string): void {
    navigator.clipboard.writeText(text).then(() => {
      // Optional: Show some feedback to the user
    }).catch(err => {
      console.error('Could not copy text: ', err);
    });
  }
}
