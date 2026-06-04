import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <main class="min-vh-100">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [],
})
export class App {
}
