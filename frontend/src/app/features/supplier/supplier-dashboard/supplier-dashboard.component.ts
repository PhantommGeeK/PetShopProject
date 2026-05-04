import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { SupplierService } from '../../../core/services/supplier.service';
import { PetService } from '../../../core/services/pet.service';
import { ToastService } from '../../../core/services/toast.service';
import { PetResponseDTO, SupplierRequestDTO, SupplierResponseDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';

type SearchMode = 'name' | 'city' | 'id';

@Component({
  selector: 'app-supplier-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe],
  templateUrl: './supplier-dashboard.component.html',
  styleUrls: ['./supplier-dashboard.component.scss']
})
export class SupplierDashboardComponent implements OnInit {
  supplierId: number | null = null;
  profile: SupplierResponseDTO | null = null;
  suppliedPets: PetResponseDTO[] = [];
  allPets: PetResponseDTO[] = [];
  searchResults: SupplierResponseDTO[] = [];

  selectedPetId: number | null = null;
  searchMode: SearchMode = 'name';
  searchTerm = '';
  loading = true;
  saving = false;

  form: SupplierRequestDTO = {
    name: '',
    contactPerson: '',
    phoneNumber: '',
    email: '',
    addressId: null,
    address: { street: '', city: '', state: '', zipCode: '' }
  };

  constructor(
    private authService: AuthService,
    private supplierService: SupplierService,
    private petService: PetService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    const profileId = this.authService.getProfileId();
    if (profileId) {
      this.initialize(profileId);
      return;
    }

    this.authService.getMe().subscribe({
      next: user => {
        if (!user.profileId) {
          this.loading = false;
          this.toast.error('Supplier profile was not found for this account.');
          return;
        }
        this.initialize(user.profileId);
      },
      error: e => {
        this.loading = false;
        this.toast.handleHttpError(e);
      }
    });
  }

  initialize(id: number): void {
    this.supplierId = id;
    this.loadProfile();
    this.loadSuppliedPets();
    this.loadAllPets();
  }

  loadProfile(): void {
    if (!this.supplierId) return;
    this.supplierService.getById(this.supplierId).subscribe({
      next: supplier => {
        this.profile = supplier;
        this.form = {
          name: supplier.name,
          contactPerson: supplier.contactPerson,
          phoneNumber: supplier.phoneNumber,
          email: supplier.email,
          addressId: null,
          address: {
            street: supplier.address?.street || '',
            city: supplier.address?.city || '',
            state: supplier.address?.state || '',
            zipCode: supplier.address?.zipCode || ''
          }
        };
        this.loading = false;
      },
      error: e => {
        this.loading = false;
        this.toast.handleHttpError(e);
      }
    });
  }

  loadSuppliedPets(): void {
    if (!this.supplierId) return;
    this.supplierService.getPets(this.supplierId).subscribe({
      next: pets => this.suppliedPets = pets,
      error: e => this.toast.handleHttpError(e)
    });
  }

  loadAllPets(): void {
    this.petService.getAll().subscribe({
      next: pets => this.allPets = pets,
      error: e => this.toast.handleHttpError(e)
    });
  }

  saveProfile(): void {
    if (!this.supplierId) return;
    this.saving = true;
    this.supplierService.update(this.supplierId, this.form).subscribe({
      next: () => {
        this.saving = false;
        this.toast.success('Profile updated.');
        this.loadProfile();
      },
      error: e => {
        this.saving = false;
        this.toast.handleHttpError(e);
      }
    });
  }

  assignPet(): void {
    if (!this.supplierId || !this.selectedPetId) return;
    this.supplierService.assignPet(this.supplierId, this.selectedPetId).subscribe({
      next: () => {
        this.toast.success('Pet assigned.');
        this.selectedPetId = null;
        this.loadSuppliedPets();
      },
      error: e => this.toast.handleHttpError(e)
    });
  }

  removePet(petId: number): void {
    if (!this.supplierId) return;
    this.supplierService.removePet(this.supplierId, petId).subscribe({
      next: () => {
        this.toast.success('Pet removed.');
        this.loadSuppliedPets();
      },
      error: e => this.toast.handleHttpError(e)
    });
  }

  searchSuppliers(): void {
    const term = this.searchTerm.trim();
    if (!term) {
      this.searchResults = [];
      return;
    }

    if (this.searchMode === 'name') {
      this.supplierService.searchByName(term).subscribe({
        next: suppliers => this.searchResults = suppliers,
        error: e => this.toast.handleHttpError(e)
      });
      return;
    }

    if (this.searchMode === 'city') {
      this.supplierService.searchByCity(term).subscribe({
        next: suppliers => this.searchResults = suppliers,
        error: e => this.toast.handleHttpError(e)
      });
      return;
    }

    const supplierId = Number(term);
    if (!Number.isInteger(supplierId) || supplierId <= 0) {
      this.toast.error('Enter a valid supplier ID.');
      return;
    }

    this.supplierService.getById(supplierId).subscribe({
      next: supplier => this.searchResults = [supplier],
      error: e => this.toast.handleHttpError(e)
    });
  }

  availablePets(): PetResponseDTO[] {
    const assigned = new Set(this.suppliedPets.map(pet => pet.petId));
    return this.allPets.filter(pet => !assigned.has(pet.petId));
  }
}
