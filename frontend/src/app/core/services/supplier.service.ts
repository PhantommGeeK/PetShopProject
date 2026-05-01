import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupplierResponseDTO, SupplierRequestDTO, PetResponseDTO, SuccessDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class SupplierService {
  private baseUrl = 'http://localhost:8080/api/suppliers';

  constructor(private http: HttpClient) {}

  getAll(): Observable<SupplierResponseDTO[]> {
    return this.http.get<SupplierResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<SupplierResponseDTO> {
    return this.http.get<SupplierResponseDTO>(`${this.baseUrl}/${id}`);
  }

  getPets(id: number): Observable<PetResponseDTO[]> {
    return this.http.get<PetResponseDTO[]>(`${this.baseUrl}/${id}/pets`);
  }

  searchByName(name: string): Observable<SupplierResponseDTO[]> {
    return this.http.get<SupplierResponseDTO[]>(`${this.baseUrl}/search`, {
      params: new HttpParams().set('name', name)
    });
  }

  searchByCity(city: string): Observable<SupplierResponseDTO[]> {
    return this.http.get<SupplierResponseDTO[]>(`${this.baseUrl}/city`, {
      params: new HttpParams().set('city', city)
    });
  }

  update(id: number, supplier: SupplierRequestDTO): Observable<SuccessDTO> {
    return this.http.put<SuccessDTO>(`${this.baseUrl}/${id}`, supplier);
  }

  delete(id: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${id}`);
  }

  assignPet(supplierId: number, petId: number): Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(`${this.baseUrl}/${supplierId}/pets/${petId}`, {});
  }

  removePet(supplierId: number, petId: number): Observable<SuccessDTO> {
    return this.http.delete<SuccessDTO>(`${this.baseUrl}/${supplierId}/pets/${petId}`);
  }
}
