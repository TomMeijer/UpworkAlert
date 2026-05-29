import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobService } from '../job.service';
import { Job, Page } from '../job.model';
import {BsModalService, BsModalRef, ModalModule} from 'ngx-bootstrap/modal';
import { JobChatModalComponent } from '../../chat/job-chat/job-chat-modal';
import { TimeAgoPipe } from '../../../util/time-ago.pipe';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalModule, TimeAgoPipe],
  providers: [BsModalService],
  templateUrl: './job-list.html',
  styleUrl: './job-list.scss'
})
export class JobListComponent implements OnInit {
  jobsPage = signal<Page<Job> | undefined>(undefined);
  currentPage = signal<number>(1);
  pageSize = 10;
  sort = 'publishedOn.desc';
  bsModalRef?: BsModalRef;
  expandedJobIds = signal<Record<number, boolean>>({});

  constructor(
    private readonly jobService: JobService,
    private readonly modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.jobService.getJobs(this.currentPage(), this.pageSize, this.sort)
      .subscribe(page => {
        this.jobsPage.set(page);
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
    this.expandedJobIds.update(ids => ({
      ...ids,
      [jobId]: !ids[jobId]
    }));
  }
}
