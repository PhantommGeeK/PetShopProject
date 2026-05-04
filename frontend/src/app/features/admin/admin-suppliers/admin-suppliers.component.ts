import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierService } from '../../../core/services/supplier.service';
import { PetService } from '../../../core/services/pet.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { SupplierResponseDTO, SupplierRequestDTO, PetResponseDTO } from '../../../core/models/api.models';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-suppliers',
  standalone: true,
  imports: [CommonModule, FormsModule, ConfirmDialogComponent],
  templateUrl: './admin-suppliers.component.html',})
export class AdminSuppliersComponent implements OnInit {
  items: SupplierResponseDTO[] = []; filtered: SupplierResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false; showPetsModal = false;
  editingId: number | null = null; deleteId: number | null = null;
  selectedSupplierId: number | null = null; assignPetId: number = 0;
  supplierPets: PetResponseDTO[] = [];
  form: SupplierRequestDTO = { name: '', contactPerson: '', phoneNumber: '', email: '', addressId: 1 };

  constructor(private svc: SupplierService, private petSvc: PetService, private toast: ToastService, public authService: AuthService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(s => s.name.toLowerCase().includes(t) || s.contactPerson.toLowerCase().includes(t)); }

  edit(s: SupplierResponseDTO): void {
    if (!this.authService.isAdmin()) return;
    this.editingId = s.supplierId;
    this.form = { name: s.name, contactPerson: s.contactPerson, phoneNumber: s.phoneNumber, email: s.email, addressId: s.address?.addressId || 1 };
    this.showModal = true;
  }
  save(): void {
    if (!this.editingId) return;
    this.svc.update(this.editingId, this.form).subscribe({ next: () => { this.toast.success('Updated!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(s: SupplierResponseDTO): void { this.deleteId = s.supplierId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }

  managePets(s: SupplierResponseDTO): void {
    this.selectedSupplierId = s.supplierId;
    this.svc.getPets(s.supplierId).subscribe({ next: d => { this.supplierPets = d; this.showPetsModal = true; }, error: () => { this.supplierPets = []; this.showPetsModal = true; } });
  }
  assignPet(): void {
    if (!this.authService.isAdmin()) return;
    if (!this.selectedSupplierId || !this.assignPetId) return;
    this.svc.assignPet(this.selectedSupplierId, this.assignPetId).subscribe({
      next: () => { this.toast.success('Pet assigned!'); this.managePets({ supplierId: this.selectedSupplierId } as any); this.assignPetId = 0; },
      error: e => this.toast.handleHttpError(e)
    });
  }
  removePet(petId: number): void {
    if (!this.authService.isAdmin()) return;
    if (!this.selectedSupplierId) return;
    this.svc.removePet(this.selectedSupplierId, petId).subscribe({
      next: () => { this.toast.success('Pet removed!'); this.managePets({ supplierId: this.selectedSupplierId } as any); },
      error: e => this.toast.handleHttpError(e)
    });
  }
}
