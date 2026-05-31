export interface Job {
  id: number;
  title: string;
  description: string | null;
  type: JobType;
  status: JobStatus | null;
  hourlyRateMin: number | null;
  hourlyRateMax: number | null;
  fixedPrice: number | null;
  clientCountry: string | null;
  requiredSkills: string[] | null;
  url: string;
  publishedOn: string;
  experienceLevel: string | null;
  paymentVerified: boolean | null;
  clientRating: number | null;
  clientTotalSpent: number | null;
}

export enum JobType {
  FIXED = 'FIXED',
  HOURLY = 'HOURLY'
}

export enum JobStatus {
  NEW = 'NEW',
  APPLIED = 'APPLIED'
}

export interface Page<T> {
  content: T[];
  page: {totalPages: number, number: number}
}
