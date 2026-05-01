import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PetResponseDTO, PetRequestDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class PetService {
  private baseUrl = 'http://localhost:8080/api/pets';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<PetResponseDTO> {
    return this.http.get<PetResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getByCategory(categoryId: number): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(`${this.baseUrl}/category/${categoryId}`);
  }

  searchByName(name: string): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(`${this.baseUrl}/search`, {
      params: new HttpParams().set('name', name)
    });
  }

  searchByBreed(breed: string): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(`${this.baseUrl}/search`, {
      params: new HttpParams().set('breed', breed)
    });
  }

  searchByAge(age: number): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(`${this.baseUrl}/search`, {
      params: new HttpParams().set('age', age.toString())
    });
  }

  searchByPriceRange(minPrice: number, maxPrice: number): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(`${this.baseUrl}/search`, {
      params: new HttpParams()
        .set('minPrice', minPrice.toString())
        .set('maxPrice', maxPrice.toString())
    });
  }

  create(pet: PetRequestDTO): Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(this.baseUrl, pet);
  }

  update(id: number, pet: PetRequestDTO): Observable<SuccessDTO> {
    return this.http.put<SuccessDTO>(`${this.baseUrl}/${id}`, pet);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }
}
