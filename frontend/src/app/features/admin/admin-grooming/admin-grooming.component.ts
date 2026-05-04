import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GroomingService } from '../../../core/services/grooming.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { GroomingResponseDTO, GroomingRequestDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-grooming',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './admin-grooming.component.html',})
export class AdminGroomingComponent implements OnInit {
  items: GroomingResponseDTO[] = []; filtered: GroomingResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: GroomingRequestDTO = { name: '', description: '', price: 0, available: true };

  constructor(private svc: GroomingService, private toast: ToastService, public authService: AuthService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(s => s.name.toLowerCase().includes(t)); }
  openModal(s?: GroomingResponseDTO): void {
    if (s) { this.editingId = s.serviceId; this.form = { name: s.name, description: s.description, price: s.price, available: s.available }; }
    else { this.editingId = null; this.form = { name: '', description: '', price: 0, available: true }; }
    this.showModal = true;
  }
  edit(s: GroomingResponseDTO): void { this.openModal(s); }
  save(): void {
    const obs = this.editingId ? this.svc.update(this.editingId, this.form) : this.svc.create(this.form);
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(s: GroomingResponseDTO): void { this.deleteId = s.serviceId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }
}
