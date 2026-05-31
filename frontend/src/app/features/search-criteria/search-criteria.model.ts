export interface SearchCriteria {
  minHourlyRate: number | null;
  minFixedPrice: number | null;
  categoryIds: string[];
  locations: string[];
  searchExpression: string;
}
