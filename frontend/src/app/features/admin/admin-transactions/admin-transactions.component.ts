import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../../core/services/transaction.service';
import { ToastService } from '../../../core/services/toast.service';
import { TransactionResponseDTO } from '../../../core/models/api.models';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';

@Component({
  selector: 'app-admin-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe],
  templateUrl: './admin-transactions.component.html',
  styleUrls: ['./admin-transactions.component.scss']})
export class AdminTransactionsComponent implements OnInit {
  transactions: TransactionResponseDTO[] = [];
  statusFilter = '';

  constructor(private svc: TransactionService, private toast: ToastService) {}
  ngOnInit(): void { this.load(); }

  load(): void { this.svc.getAll().subscribe({ next: d => this.transactions = d, error: e => this.toast.handleHttpError(e) }); }

  onStatusChange(): void {
    if (this.statusFilter) {
      this.svc.getByStatus(this.statusFilter).subscribe({ next: d => this.transactions = d, error: e => this.toast.handleHttpError(e) });
    } else { this.load(); }
  }

  updateStatus(id: number, status: string): void {
    this.svc.updateStatus(id, status).subscribe({
      next: () => this.toast.success('Status updated!'),
      error: e => this.toast.handleHttpError(e)
    });
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'SUCCESS': return 'badge-completed';
      case 'FAILED': return 'badge-cancelled';
      default: return '';
    }
  }
}
