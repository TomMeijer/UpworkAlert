import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobService } from '../job.service';
import { Job, JobStatus, Page } from '../job.model';
import {BsModalService, BsModalRef, ModalModule} from 'ngx-bootstrap/modal';
import { JobChatModalComponent } from '../../chat/job-chat/job-chat-modal';
import { TimeAgoPipe } from '../../../util/time-ago.pipe';

interface JobContext {
  expanded: boolean;
  isDiscarding: boolean;
  isUpdatingStatus: boolean;
}

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalModule, TimeAgoPipe],
  providers: [BsModalService],
  templateUrl: './job-list.html',
  styleUrl: './job-list.scss'
})
export class JobListComponent implements OnInit {
  isLoading = signal<boolean>(false);
  jobsPage = signal<Page<Job> | undefined>(undefined);
  currentPage = signal<number>(1);
  pageSize = 10;
  sort = 'publishedOn.desc';
  bsModalRef?: BsModalRef;
  jobContexts = signal<Record<number, JobContext>>({});
  error = signal<string | undefined>(undefined);

  constructor(
    private readonly jobService: JobService,
    private readonly modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.isLoading.set(true);
    this.loadJobs();
  }

  loadJobs(): void {
    this.jobService.getJobs(this.currentPage(), this.pageSize, this.sort).subscribe({
      next: (page) => {
        this.isLoading.set(false);
        this.jobsPage.set(page);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set('Failed to load jobs. Please try again later.')
      }
    });
  }

  openChatModal(job: Job): void {
    const initialState = {setJob: job};
    this.bsModalRef = this.modalService.show(JobChatModalComponent, { initialState, class: 'modal-lg' });
  }

  onPageChange(page: number): void {
    if (page < 1 || (this.jobsPage() && page > this.jobsPage()!.totalPages)) return;
    this.currentPage.set(page);
    this.loadJobs();
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadJobs();
  }

  onSortChange(): void {
    this.currentPage.set(1);
    this.loadJobs();
  }

  getRatingStars(rating: number): number[] {
    return new Array(Math.round(rating || 0)).fill(0);
  }

  toggleDescription(jobId: number): void {
    this.jobContexts.update(contexts => ({
      ...contexts,
      [jobId]: {
        ...this.getJobContext(jobId, contexts),
        expanded: !this.getJobContext(jobId, contexts).expanded
      }
    }));
  }

  private getJobContext(jobId: number, contexts: Record<number, JobContext> = this.jobContexts()): JobContext {
    return contexts[jobId] || { expanded: false, isDiscarding: false, isUpdatingStatus: false };
  }

  discardJob(jobId: number): void {
    if (confirm('Are you sure you want to discard this job?')) {
      this.updateJobContext(jobId, { isDiscarding: true });
      this.jobService.discardJob(jobId).subscribe({
        next: () => {
          this.updateJobContext(jobId, { isDiscarding: false });
          this.loadJobs();
        },
        error: (err) => {
          this.updateJobContext(jobId, { isDiscarding: false });
        }
      });
    }
  }

  toggleApplied(job: Job): void {
    const newStatus = job.status === JobStatus.APPLIED ? JobStatus.NEW : JobStatus.APPLIED;
    this.updateJobContext(job.id, { isUpdatingStatus: true });
    this.jobService.updateJobStatus(job.id, newStatus).subscribe({
      next: () => {
        this.updateJobContext(job.id, { isUpdatingStatus: false });
        job.status = newStatus;
      },
      error: (err) => {
        this.updateJobContext(job.id, { isUpdatingStatus: false });
      }
    });
  }

  private updateJobContext(jobId: number, update: Partial<JobContext>): void {
    this.jobContexts.update(contexts => ({
      ...contexts,
      [jobId]: {
        ...this.getJobContext(jobId, contexts),
        ...update
      }
    }));
  }
}
