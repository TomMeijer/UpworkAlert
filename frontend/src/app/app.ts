import { Component } from '@angular/core';
import { JobListComponent } from './features/jobs/job-list/job-list';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [JobListComponent],
  template: `
    <main class="min-vh-100">
      <app-job-list></app-job-list>
    </main>
  `,
  styles: [],
})
export class App {
}
