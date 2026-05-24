export enum JobType {
  HOURLY = 'HOURLY',
  FIXED = 'FIXED'
}

export interface Job {
  id: string;
  title: string;
  description: string;
  type: JobType;
  hourlyRateMin?: number;
  hourlyRateMax?: number;
  fixedPrice?: number;
  clientCountry: string;
  requiredSkills: string[];
  url: string;
  publishedOn: string;
  experienceLevel: string;
  paymentVerified: boolean;
  clientRating: number;
  clientTotalSpent: number;
  priceString: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
}
