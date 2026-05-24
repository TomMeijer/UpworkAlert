import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobService } from '../job.service';
import { Job, Page } from '../job.model';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './job-list.html',
  styleUrl: './job-list.scss'
})
export class JobListComponent implements OnInit {
  jobsPage = signal<Page<Job> | undefined>(undefined);
  currentPage = 1;
  pageSize = 10;
  sort = 'publishedOn.desc';

  constructor(private readonly jobService: JobService) {}

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.jobService.getJobs(this.currentPage, this.pageSize, this.sort)
      .subscribe(page => {
        this.jobsPage.set(page);
      });
  }

  onPageChange(page: number): void {
    if (page < 1 || (this.jobsPage() && page > this.jobsPage()!.totalPages)) return;
    this.currentPage = page;
    this.loadJobs();
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.loadJobs();
  }

  onSortChange(): void {
    this.currentPage = 1;
    this.loadJobs();
  }

  getRatingStars(rating: number): number[] {
    return new Array(Math.round(rating || 0)).fill(0);
  }

  truncateDescription(description: string): string {
    if (!description) return '';
    return description.length > 400
      ? description.substring(0, 400) + '...'
      : description;
  }
}
