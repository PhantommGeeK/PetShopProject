import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VaccinationService } from '../../../core/services/vaccination.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { VaccinationResponseDTO, VaccinationRequestDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-vaccinations',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './admin-vaccinations.component.html',})
export class AdminVaccinationsComponent implements OnInit {
  items: VaccinationResponseDTO[] = []; filtered: VaccinationResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: VaccinationRequestDTO = { name: '', description: '', price: 0, available: true };

  constructor(private svc: VaccinationService, private toast: ToastService, public authService: AuthService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(v => v.name.toLowerCase().includes(t)); }
  openModal(v?: VaccinationResponseDTO): void {
    if (v) { this.editingId = v.vaccinationId; this.form = { name: v.name, description: v.description, price: v.price, available: v.available }; }
    else { this.editingId = null; this.form = { name: '', description: '', price: 0, available: true }; }
    this.showModal = true;
  }
  edit(v: VaccinationResponseDTO): void { this.openModal(v); }
  save(): void {
    const obs = this.editingId ? this.svc.update(this.editingId, this.form) : this.svc.create(this.form);
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(v: VaccinationResponseDTO): void { this.deleteId = v.vaccinationId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }
}
