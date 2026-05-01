import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransactionResponseDTO, TransactionRequestDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private baseUrl = 'http://localhost:8080/api/transactions';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TransactionResponseDTO[]> {
    return this.http.get<TransactionResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<TransactionResponseDTO> {
    return this.http.get<TransactionResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getByStatus(status: string): Observable<TransactionResponseDTO[]> {
    return this.http.get<TransactionResponseDTO[]>(`${this.baseUrl}/status/${status}`);
  }

  create(transaction: TransactionRequestDTO): Observable<TransactionResponseDTO> {
    return this.http.post<TransactionResponseDTO>(this.baseUrl, transaction);
  }

  updateStatus(id: number, status: string): Observable<TransactionResponseDTO> {
    return this.http.put<TransactionResponseDTO>(`${this.baseUrl}/${id}/status`, null, {
      params: new HttpParams().set('status', status)
    });
  }
}
