import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PetFoodService } from '../../../core/services/pet-food.service';
import { ToastService } from '../../../core/services/toast.service';
import { PetFoodResponseDTO, PetFoodRequestDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-pet-food',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './admin-pet-food.component.html',})
export class AdminPetFoodComponent implements OnInit {
  items: PetFoodResponseDTO[] = []; filtered: PetFoodResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: PetFoodRequestDTO = { name: '', brand: '', type: '', quantity: 0, price: 0 };

  constructor(private svc: PetFoodService, private toast: ToastService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(f => f.name.toLowerCase().includes(t) || f.brand.toLowerCase().includes(t)); }
  openModal(f?: PetFoodResponseDTO): void {
    if (f) { this.editingId = f.foodId; this.form = { name: f.name, brand: f.brand, type: f.type, quantity: f.quantity, price: f.price }; }
    else { this.editingId = null; this.form = { name: '', brand: '', type: '', quantity: 0, price: 0 }; }
    this.showModal = true;
  }
  edit(f: PetFoodResponseDTO): void { this.openModal(f); }
  save(): void {
    const obs = this.editingId ? this.svc.update(this.editingId, this.form) : this.svc.create(this.form);
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(f: PetFoodResponseDTO): void { this.deleteId = f.foodId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }
}
