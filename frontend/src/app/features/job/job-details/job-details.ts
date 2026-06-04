import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { JobService } from '../job.service';
import { Job, JobStatus, JobType } from '../job.model';
import { BsModalService, BsModalRef, ModalModule } from 'ngx-bootstrap/modal';
import { JobChatModalComponent } from '../../chat/job-chat/job-chat-modal';
import { TimeAgoPipe } from '../../../util/time-ago.pipe';

@Component({
  selector: 'app-job-details',
  standalone: true,
  imports: [CommonModule, RouterModule, ModalModule, TimeAgoPipe, DecimalPipe],
  providers: [BsModalService],
  templateUrl: './job-details.html',
  styleUrl: './job-details.scss'
})
export class JobDetailsComponent implements OnInit {
  job = signal<Job | undefined>(undefined);
  isLoading = signal<boolean>(true);
  error = signal<string | undefined>(undefined);
  isUpdatingStatus = signal<boolean>(false);
  isDiscarding = signal<boolean>(false);
  bsModalRef?: BsModalRef;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly jobService: JobService,
    private readonly modalService: BsModalService
  ) {}

  ngOnInit(): void {
    const jobId = this.route.snapshot.paramMap.get('jobId');
    if (jobId) {
      this.loadJob(Number(jobId));
    } else {
      this.error.set('No job ID provided');
      this.isLoading.set(false);
    }
  }

  loadJob(id: number): void {
    this.isLoading.set(true);
    this.jobService.getJob(id).subscribe({
      next: (job) => {
        this.job.set(job);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load job details. Please try again later.');
        this.isLoading.set(false);
      }
    });
  }

  openChatModal(): void {
    const currentJob = this.job();
    if (!currentJob) return;
    const initialState = { setJob: currentJob };
    this.bsModalRef = this.modalService.show(JobChatModalComponent, { initialState, class: 'modal-lg' });
  }

  toggleApplied(): void {
    const currentJob = this.job();
    if (!currentJob) return;
    const newStatus = currentJob.status === JobStatus.APPLIED ? JobStatus.NEW : JobStatus.APPLIED;
    this.isUpdatingStatus.set(true);
    this.jobService.updateJobStatus(currentJob.id, newStatus).subscribe({
      next: () => {
        this.isUpdatingStatus.set(false);
        this.job.update(j => j ? { ...j, status: newStatus } : undefined);
      },
      error: (err) => {
        this.isUpdatingStatus.set(false);
      }
    });
  }

  discardJob(): void {
    const currentJob = this.job();
    if (!currentJob) return;
    if (confirm('Are you sure you want to discard this job?')) {
      this.isDiscarding.set(true);
      this.jobService.discardJob(currentJob.id).subscribe({
        next: () => {
          this.isDiscarding.set(false);
          this.router.navigate(['/jobs']);
        },
        error: (err) => {
          this.isDiscarding.set(false);
        }
      });
    }
  }

  getRatingStars(rating: number): number[] {
    return new Array(Math.round(rating || 0)).fill(0);
  }

  formatPrice(job: Job): string {
    if (job.type == JobType.FIXED) {
      return 'Fixed: $' + job.fixedPrice;
    } else if (job.type == JobType.HOURLY) {
      let hourlyString = 'Hourly: $' + job.hourlyRateMin;
      if (job.hourlyRateMax != null) {
        hourlyString += ' - $' + job.hourlyRateMax;
      }
      return hourlyString;
    }
    return 'Not specified';
  }
}
