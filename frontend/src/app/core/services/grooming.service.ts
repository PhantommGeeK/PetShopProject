import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroomingResponseDTO, GroomingRequestDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class GroomingService {
  private baseUrl = 'http://localhost:8080/api/grooming';

  constructor(private http: HttpClient) {}

  getAll(): Observable<GroomingResponseDTO[]> {
    return this.http.get<GroomingResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<GroomingResponseDTO> {
    return this.http.get<GroomingResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getAvailable(): Observable<GroomingResponseDTO[]> {
    return this.http.get<GroomingResponseDTO[]>(`${this.baseUrl}/available`);
  }

  create(service: GroomingRequestDTO): Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(this.baseUrl, service);
  }

  update(id: number, service: GroomingRequestDTO): Observable<SuccessDTO> {
    return this.http.put<SuccessDTO>(`${this.baseUrl}/${id}`, service);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }
}
