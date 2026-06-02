import {Component, OnInit, signal} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, NgForm} from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { NotificationSettings } from '../notification-settings.model';
import { NotificationSettingsService } from '../notification-settings.service';

@Component({
  selector: 'app-notification-settings-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notification-settings-modal.html',
  styleUrl: './notification-settings-modal.scss'
})
export class NotificationSettingsModalComponent implements OnInit {
  settings = signal<NotificationSettings>({
    emailEnabled: false,
    recipientEmail: ''
  });
  isLoading = signal<boolean>(false);
  isSaving = signal<boolean>(false);
  formInvalid = signal<boolean>(false);
  error = signal<string | undefined>(undefined);

  constructor(
    public bsModalRef: BsModalRef,
    private readonly notificationSettingsService: NotificationSettingsService
  ) {}

  ngOnInit(): void {
    this.loadSettings();
  }

  loadSettings(): void {
    this.isLoading.set(true);
    this.notificationSettingsService.getNotificationSettings().subscribe({
      next: (settings) => {
        this.settings.set(settings);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load notification settings.');
        this.isLoading.set(false);
      }
    });
  }

  save(form: NgForm): void {
    this.formInvalid.set(!form.valid);
    if (this.formInvalid()) return;
    this.isSaving.set(true);
    this.notificationSettingsService.updateNotificationSettings(this.settings()).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.bsModalRef.hide();
      },
      error: (err) => {
        this.error.set('Failed to save notification settings.');
        this.isSaving.set(false);
      }
    });
  }
}
