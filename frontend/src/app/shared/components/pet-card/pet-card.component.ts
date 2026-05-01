import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PetResponseDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../pipes/currency-inr.pipe';
import { CartService } from '../../../core/services/cart.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-pet-card',
  standalone: true,
  imports: [CommonModule, RouterModule, CurrencyInrPipe],
  templateUrl: './pet-card.component.html',
  styleUrls: ['./pet-card.component.scss']})
export class PetCardComponent {
  @Input() pet!: PetResponseDTO;

  constructor(
    private readonly cartService: CartService,
    private readonly toast: ToastService
  ) {}

  addToCart(): void {
    this.cartService.addPet(this.pet);
    this.toast.success(`${this.pet.name} added to cart`);
  }
}
