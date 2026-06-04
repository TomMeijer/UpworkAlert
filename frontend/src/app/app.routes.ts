import { Routes } from '@angular/router';
import { JobListComponent } from './features/job/job-list/job-list';

export const routes: Routes = [
  { path: '', redirectTo: 'jobs', pathMatch: 'full' },
  { path: 'jobs', component: JobListComponent },
  { path: 'jobs/:jobId', loadComponent: () => import('./features/job/job-details/job-details').then(m => m.JobDetailsComponent) }
];
