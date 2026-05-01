import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VaccinationResponseDTO, VaccinationRequestDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class VaccinationService {
  private baseUrl = 'http://localhost:8080/api/vaccinations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<VaccinationResponseDTO[]> {
    return this.http.get<VaccinationResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<VaccinationResponseDTO> {
    return this.http.get<VaccinationResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getAvailable(): Observable<VaccinationResponseDTO[]> {
    return this.http.get<VaccinationResponseDTO[]>(`${this.baseUrl}/available`);
  }

  create(vaccination: VaccinationRequestDTO): Observable<VaccinationResponseDTO> {
    return this.http.post<VaccinationResponseDTO>(this.baseUrl, vaccination);
  }

  update(id: number, vaccination: VaccinationRequestDTO): Observable<VaccinationResponseDTO> {
    return this.http.put<VaccinationResponseDTO>(`${this.baseUrl}/${id}`, vaccination);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}
