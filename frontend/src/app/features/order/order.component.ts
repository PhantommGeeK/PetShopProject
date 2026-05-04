import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CartItem, CartService } from '../../core/services/cart.service';
import { AuthService } from '../../core/services/auth.service';
import { TransactionService } from '../../core/services/transaction.service';
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
  placingOrder = false;

  constructor(
    private readonly cartService: CartService,
    private readonly authService: AuthService,
    private readonly transactionService: TransactionService,
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

    const customerId = this.authService.getProfileId();
    if (!this.authService.isCustomer() || !customerId) {
      this.toast.error('Please log in as a customer to place an order');
      this.router.navigate(['/login']);
      return;
    }

    const today = new Date().toISOString().slice(0, 10);
    const requests = this.items.map(item => this.transactionService.create({
      transactionDate: today,
      amount: item.price * item.quantity,
      transactionStatus: 'Success',
      itemType: item.itemType,
      itemName: item.name,
      quantity: item.quantity,
      customerId,
      petId: item.itemType === 'pet' ? item.itemId : null,
      foodId: item.itemType === 'food' ? item.itemId : null,
      groomingServiceId: item.itemType === 'grooming' ? item.itemId : null,
      vaccinationId: item.itemType === 'vaccination' ? item.itemId : null
    }));

    this.placingOrder = true;
    forkJoin(requests).subscribe({
      next: () => {
        this.placingOrder = false;
        this.cartService.clear();
        this.toast.success('Order placed successfully');
        this.router.navigate(['/dashboard']);
      },
      error: error => {
        this.placingOrder = false;
        this.toast.handleHttpError(error);
      }
    });
  }
}
