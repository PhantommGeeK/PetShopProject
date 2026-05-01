import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeResponseDTO, EmployeeRequestDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private baseUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) {}

  getAll(): Observable<EmployeeResponseDTO[]> {
    return this.http.get<EmployeeResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<EmployeeResponseDTO> {
    return this.http.get<EmployeeResponseDTO>(`${this.baseUrl}/${id}`);
  }

  update(id: number, employee: EmployeeRequestDTO): Observable<SuccessDTO> {
    return this.http.put<SuccessDTO>(`${this.baseUrl}/${id}`, employee);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }
}
