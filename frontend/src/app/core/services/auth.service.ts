import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { LoginRequestDTO, LoginResponseDTO, UserInfo, SuccessDTO, CustomerRegisterDTO, EmployeeRegisterDTO, SupplierRegisterDTO } from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/auth';

  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  private currentRole = new BehaviorSubject<string>(this.getRole());

  isLoggedIn$ = this.loggedIn.asObservable();
  currentRole$ = this.currentRole.asObservable();

  constructor(private http: HttpClient) {}

  private hasToken(): boolean {
    return !!localStorage.getItem('petshop_token');
  }

  login(credentials: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${this.baseUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('petshop_token', response.token);
        // Decode JWT to extract role
        const payload = this.decodeToken(response.token);
        if (payload) {
          const role = payload.role || payload.authorities?.[0]?.authority || 'ROLE_CUSTOMER';
          const username = payload.sub || payload.username || credentials.username;
          localStorage.setItem('petshop_role', role);
          localStorage.setItem('petshop_username', username);
          if (payload.userId) {
            localStorage.setItem('petshop_user_id', String(payload.userId));
          }
          if (payload.profileId) {
            localStorage.setItem('petshop_profile_id', String(payload.profileId));
          }
        }
        this.loggedIn.next(true);
        this.currentRole.next(this.getRole());
      })
    );
  }

  registerCustomer(data: CustomerRegisterDTO): Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(`${this.baseUrl}/register/customer`, data);
  }

  registerSupplier(data: SupplierRegisterDTO): Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(`${this.baseUrl}/register/supplier`, data);
  }

  registerEmployee(data: EmployeeRegisterDTO): Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(`${this.baseUrl}/register/employee`, data);
  }

  getMe(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/me`).pipe(
      tap(user => {
        if (user?.username) {
          localStorage.setItem('petshop_username', user.username);
        }
        if (user?.role) {
          localStorage.setItem('petshop_role', user.role);
        }
        if (user?.userId) {
          localStorage.setItem('petshop_user_id', String(user.userId));
        }
        if (user?.profileId) {
          localStorage.setItem('petshop_profile_id', String(user.profileId));
        }
        this.currentRole.next(this.getRole());
      })
    );
  }

  logout(): void {
    localStorage.removeItem('petshop_token');
    localStorage.removeItem('petshop_role');
    localStorage.removeItem('petshop_username');
    localStorage.removeItem('petshop_user_id');
    localStorage.removeItem('petshop_profile_id');
    this.loggedIn.next(false);
    this.currentRole.next('');
  }

  isLoggedIn(): boolean {
    return this.hasToken();
  }

  getRole(): string {
    return localStorage.getItem('petshop_role') || '';
  }

  getUsername(): string {
    return localStorage.getItem('petshop_username') || '';
  }

  getProfileId(): number | null {
    const profileId = localStorage.getItem('petshop_profile_id');
    return profileId ? Number(profileId) : null;
  }

  getToken(): string | null {
    return localStorage.getItem('petshop_token');
  }

  isAdmin(): boolean {
    return this.getRole() === 'ROLE_ADMIN';
  }

  isEmployee(): boolean {
    return this.getRole() === 'ROLE_EMPLOYEE';
  }

  isSupplier(): boolean {
    return this.getRole() === 'ROLE_SUPPLIER';
  }

  isCustomer(): boolean {
    return this.getRole() === 'ROLE_CUSTOMER';
  }

  hasAdminAccess(): boolean {
    return this.isAdmin();
  }

  canManageOperations(): boolean {
    return this.isAdmin() || this.isEmployee();
  }

  canDeleteCatalog(): boolean {
    return this.isAdmin();
  }

  private decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch {
      return null;
    }
  }
}
