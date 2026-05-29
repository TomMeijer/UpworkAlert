export interface Job {
  id: number;
  title: string;
  description?: string;
  clientCountry?: string;
  requiredSkills?: string[];
  url: string;
  publishedOn: string;
  experienceLevel?: string;
  paymentVerified?: boolean;
  clientRating?: number;
  clientTotalSpent?: number;
  priceString: string;
  status?: JobStatus;
}

export enum JobStatus {
  NEW = 'NEW',
  APPLIED = 'APPLIED'
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  number: number;
}
