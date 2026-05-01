import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PetCategoryResponseDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class PetCategoryService {
  private baseUrl = 'http://localhost:8080/api/pet-categories';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PetCategoryResponseDTO[]> {
    return this.http.get<PetCategoryResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<PetCategoryResponseDTO> {
    return this.http.get<PetCategoryResponseDTO>(`${this.baseUrl}/${id}`);
  }

  create(data: { name: string }): Observable<PetCategoryResponseDTO> {
    return this.http.post<PetCategoryResponseDTO>(this.baseUrl, data);
  }

  update(id: number, data: { name: string }): Observable<PetCategoryResponseDTO> {
    return this.http.put<PetCategoryResponseDTO>(`${this.baseUrl}/${id}`, data);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }
}
