import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PetFoodService } from '../../core/services/pet-food.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { CartService } from '../../core/services/cart.service';
import { PetFoodResponseDTO, PetFoodRequestDTO } from '../../core/models/api.models';
import { CurrencyInrPipe } from '../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-pet-food',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './pet-food.component.html',
  styleUrls: ['./pet-food.component.scss']})
export class PetFoodComponent implements OnInit {
  foods: PetFoodResponseDTO[] = [];
  types: string[] = [];
  selectedType = '';
  loading = true;
  showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: PetFoodRequestDTO = { name: '', brand: '', type: '', quantity: 0, price: 0 };

  constructor(
    public authService: AuthService,
    private foodService: PetFoodService,
    private cartService: CartService,
    private toast: ToastService
  ) {}

  ngOnInit(): void { this.loadFoods(); }

  loadFoods(): void {
    this.loading = true;
    this.foodService.getAll().subscribe({
      next: d => { this.foods = d; this.types = [...new Set(d.map(f => f.type))]; this.loading = false; },
      error: () => this.loading = false
    });
  }

  filterByType(type: string): void {
    this.selectedType = type;
    this.loading = true;
    if (type) {
      this.foodService.getByType(type).subscribe({ next: d => { this.foods = d; this.loading = false; }, error: () => this.loading = false });
    } else {
      this.loadFoods();
    }
  }

  openModal(food?: PetFoodResponseDTO): void {
    if (food) { this.editingId = food.foodId; this.form = { name: food.name, brand: food.brand, type: food.type, quantity: food.quantity, price: food.price }; }
    else { this.editingId = null; this.form = { name: '', brand: '', type: '', quantity: 0, price: 0 }; }
    this.showModal = true;
  }

  editFood(f: PetFoodResponseDTO): void { this.openModal(f); }

  saveFood(): void {
    const obs = this.editingId ? this.foodService.update(this.editingId, this.form) : this.foodService.create(this.form);
    obs.subscribe({ next: () => { this.toast.success(this.editingId ? 'Updated!' : 'Created!'); this.showModal = false; this.loadFoods(); }, error: e => this.toast.handleHttpError(e) });
  }

  confirmDelete(f: PetFoodResponseDTO): void { this.deleteId = f.foodId; this.showConfirm = true; }

  addToCart(food: PetFoodResponseDTO): void {
    this.cartService.addFood(food);
    this.toast.success(`${food.name} added to cart`);
  }

  getFoodImage(food: PetFoodResponseDTO): string {
    const text = `${food.name} ${food.type} ${food.brand}`.toLowerCase();
    if (text.includes('fish') || text.includes('aquatic')) {
      return 'assets/images/fish-food.jpg';
    }
    if (text.includes('bird') || text.includes('avian') || text.includes('parrot')) {
      return 'assets/images/bird-food.jpg';
    }
    if (text.includes('cat')) {
      return 'assets/images/cat-food.png';
    }
    return 'assets/images/dog-food.png';
  }

  deleteFood(): void {
    if (!this.deleteId) return;
    this.foodService.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.loadFoods(); }, error: e => this.toast.handleHttpError(e) });
  }
}
