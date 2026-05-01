import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomersResponseDTO, CustomersRequestDTO, CustomerTransactionSummaryDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private baseUrl = 'http://localhost:8080/api/customers';

  constructor(private http: HttpClient) {}

  getAll(): Observable<CustomersResponseDTO[]> {
    return this.http.get<CustomersResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<CustomersResponseDTO> {
    return this.http.get<CustomersResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getByEmail(email: string): Observable<CustomersResponseDTO> {
    return this.http.get<CustomersResponseDTO>(`${this.baseUrl}/email/${email}`);
  }

  getTransactionSummary(id: number): Observable<CustomerTransactionSummaryDTO> {
    return this.http.get<CustomerTransactionSummaryDTO>(`${this.baseUrl}/${id}/transactions`);
  }

  update(id: number, customer: CustomersRequestDTO): Observable<CustomersResponseDTO> {
    return this.http.put<CustomersResponseDTO>(`${this.baseUrl}/${id}`, customer);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }
}
