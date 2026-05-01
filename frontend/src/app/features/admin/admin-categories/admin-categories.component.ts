import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PetCategoryService } from '../../../core/services/pet-category.service';
import { ToastService } from '../../../core/services/toast.service';
import { PetCategoryResponseDTO } from '../../../core/models/api.models';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-categories',
  standalone: true,
  imports: [CommonModule, FormsModule, ConfirmDialogComponent],
  templateUrl: './admin-categories.component.html',})
export class AdminCategoriesComponent implements OnInit {
  items: PetCategoryResponseDTO[] = []; filtered: PetCategoryResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null; formName = '';

  constructor(private svc: PetCategoryService, private toast: ToastService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(c => c.name.toLowerCase().includes(t)); }
  openModal(c?: PetCategoryResponseDTO): void { this.editingId = c ? c.categoryId : null; this.formName = c ? c.name : ''; this.showModal = true; }
  edit(c: PetCategoryResponseDTO): void { this.openModal(c); }
  save(): void {
    const obs = this.editingId ? this.svc.update(this.editingId, { name: this.formName }) : this.svc.create({ name: this.formName });
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(c: PetCategoryResponseDTO): void { this.deleteId = c.categoryId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }
}
