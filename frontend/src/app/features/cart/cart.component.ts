import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartItem, CartService } from '../../core/services/cart.service';
import { CurrencyInrPipe } from '../../shared/pipes/currency-inr.pipe';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, CurrencyInrPipe],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalAmount = 0;
  totalCount = 0;

  constructor(
    private readonly cartService: CartService,
    private readonly toast: ToastService
  ) {}

  ngOnInit(): void {
    this.syncSummary();
    this.cartService.items$.subscribe(() => this.syncSummary());
  }

  increase(item: CartItem): void {
    this.cartService.increase(item.id);
  }

  decrease(item: CartItem): void {
    this.cartService.decrease(item.id);
  }

  remove(item: CartItem): void {
    this.cartService.remove(item.id);
    this.toast.show(`${item.name} removed from cart`, 'info');
  }

  clearCart(): void {
    this.cartService.clear();
    this.toast.success('Cart cleared');
  }

  private syncSummary(): void {
    this.cartItems = this.cartService.getItems();
    this.totalAmount = this.cartService.getTotalAmount();
    this.totalCount = this.cartService.getTotalItemsCount();
  }
}
