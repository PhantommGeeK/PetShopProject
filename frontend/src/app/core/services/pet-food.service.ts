import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PetFoodResponseDTO, PetFoodRequestDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class PetFoodService {
  private baseUrl = 'http://localhost:8080/api/pet-foods';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PetFoodResponseDTO[]> {
    return this.http.get<PetFoodResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<PetFoodResponseDTO> {
    return this.http.get<PetFoodResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getByType(type: string): Observable<PetFoodResponseDTO[]> {
    return this.http.get<PetFoodResponseDTO[]>(`${this.baseUrl}/type/${type}`);
  }

  create(food: PetFoodRequestDTO): Observable<PetFoodResponseDTO> {
    return this.http.post<PetFoodResponseDTO>(this.baseUrl, food);
  }

  update(id: number, food: PetFoodRequestDTO): Observable<PetFoodResponseDTO> {
    return this.http.put<PetFoodResponseDTO>(`${this.baseUrl}/${id}`, food);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }
}
