import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import {SearchCriteria} from '../search-criteria.model';
import {SearchCriteriaService} from '../search-criteria.service';

@Component({
  selector: 'app-search-criteria-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-criteria-modal.html',
  styleUrl: './search-criteria-modal.scss'
})
export class SearchCriteriaModalComponent implements OnInit {
  criteria = signal<SearchCriteria>({
    minHourlyRate: null,
    minFixedPrice: null,
    categoryIds: [],
    locations: [],
    searchExpression: ''
  });
  isLoading = signal<boolean>(false);
  isSaving = signal<boolean>(false);
  error = signal<string | undefined>(undefined);

  readonly categories = [
    { name: 'Accounting & Consulting', value: '531770282584862721' },
    { name: 'Admin Support', value: '531770282580668416' },
    { name: 'Customer Service', value: '531770282580668417' },
    { name: 'Data Science & Analytics', value: '531770282580668420' },
    { name: 'Design & Creative', value: '531770282580668421' },
    { name: 'Engineering & Architecture', value: '531770282584862722' },
    { name: 'IT & Networking', value: '531770282580668419' },
    { name: 'Legal', value: '531770282584862723' },
    { name: 'Sales & Marketing', value: '531770282580668422' },
    { name: 'Translation', value: '531770282584862720' },
    { name: 'Web, Mobile & Software Dev', value: '531770282580668418' },
    { name: 'Writing', value: '531770282580668423' }
  ];

  readonly locations = ['Africa', 'Americas', 'Antarctica', 'Asia', 'Europe', 'Oceania'];

  constructor(
    public bsModalRef: BsModalRef,
    private readonly searchCriteriaService: SearchCriteriaService
  ) {}

  ngOnInit(): void {
    this.loadCriteria();
  }

  loadCriteria(): void {
    this.isLoading.set(true);
    this.searchCriteriaService.getSearchCriteria().subscribe({
      next: (criteria) => {
        this.criteria.set(criteria);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load search criteria.');
        this.isLoading.set(false);
      }
    });
  }

  save(): void {
    this.isSaving.set(true);
    this.searchCriteriaService.updateSearchCriteria(this.criteria()).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.bsModalRef.hide();
      },
      error: (err) => {
        this.error.set('Failed to save search criteria.');
        this.isSaving.set(false);
      }
    });
  }

  toggleCategory(value: string): void {
    const current = this.criteria().categoryIds;
    if (current.includes(value)) {
      this.criteria.update(c => ({ ...c, categoryIds: current.filter(id => id !== value) }));
    } else {
      this.criteria.update(c => ({ ...c, categoryIds: [...current, value] }));
    }
  }

  toggleLocation(value: string): void {
    const current = this.criteria().locations;
    if (current.includes(value)) {
      this.criteria.update(c => ({ ...c, locations: current.filter(loc => loc !== value) }));
    } else {
      this.criteria.update(c => ({ ...c, locations: [...current, value] }));
    }
  }
}
