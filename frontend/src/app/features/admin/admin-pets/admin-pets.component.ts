import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PetService } from '../../../core/services/pet.service';
import { PetCategoryService } from '../../../core/services/pet-category.service';
import { ToastService } from '../../../core/services/toast.service';
import { PetResponseDTO, PetRequestDTO, PetCategoryResponseDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-pets',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './admin-pets.component.html',
  styleUrls: ['./admin-pets.component.scss']})
export class AdminPetsComponent implements OnInit {
  pets: PetResponseDTO[] = []; filteredPets: PetResponseDTO[] = [];
  categories: PetCategoryResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: PetRequestDTO = { name: '', breed: '', age: 0, price: 0, description: '', imageUrl: '', categoryId: 1 };

  constructor(private petService: PetService, private catService: PetCategoryService, private toast: ToastService) {}
  ngOnInit(): void { this.load(); this.catService.getAll().subscribe(c => this.categories = c); }

  load(): void { this.petService.getAll().subscribe({ next: d => { this.pets = d; this.filterPets(); }, error: e => this.toast.handleHttpError(e) }); }
  filterPets(): void { const t = this.searchTerm.toLowerCase(); this.filteredPets = this.pets.filter(p => p.name.toLowerCase().includes(t) || p.breed.toLowerCase().includes(t)); }
  getCategoryName(id: number): string { return this.categories.find(c => c.categoryId === id)?.name || 'N/A'; }
  getCatId(p: PetResponseDTO): number { return p.category?.categoryId || 0; }

  openModal(p?: PetResponseDTO): void {
    if (p) { this.editingId = p.petId; this.form = { name: p.name, breed: p.breed, age: p.age, price: p.price, description: p.description, imageUrl: p.imageUrl, categoryId: p.category?.categoryId || 1 }; }
    else { this.editingId = null; this.form = { name: '', breed: '', age: 0, price: 0, description: '', imageUrl: '', categoryId: this.categories[0]?.categoryId || 1 }; }
    this.showModal = true;
  }
  editPet(p: PetResponseDTO): void { this.openModal(p); }
  save(): void {
    const obs = this.editingId ? this.petService.update(this.editingId, this.form) : this.petService.create(this.form);
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(p: PetResponseDTO): void { this.deleteId = p.petId; this.showConfirm = true; }
  deletePet(): void {
    if (!this.deleteId) return;
    this.petService.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
}
