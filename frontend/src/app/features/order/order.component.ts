import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CartItem, CartService } from '../../core/services/cart.service';
import { CurrencyInrPipe } from '../../shared/pipes/currency-inr.pipe';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, CurrencyInrPipe],
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']})
export class OrderComponent implements OnInit {
  items: CartItem[] = [];
  totalCount = 0;
  totalAmount = 0;

  customerName = '';
  phone = '';
  address = '';

  constructor(
    private readonly cartService: CartService,
    private readonly toast: ToastService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.items = this.cartService.getItems();
    this.totalCount = this.cartService.getTotalItemsCount();
    this.totalAmount = this.cartService.getTotalAmount();
  }

  placeOrder(): void {
    if (!this.customerName.trim() || !this.phone.trim() || !this.address.trim()) {
      this.toast.error('Please fill all order details');
      return;
    }

    this.cartService.clear();
    this.toast.success('Order placed successfully');
    this.router.navigate(['/']);
  }
}
