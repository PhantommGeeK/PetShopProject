import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../../core/services/customer.service';
import { ToastService } from '../../../core/services/toast.service';
import { CustomersResponseDTO, CustomersRequestDTO } from '../../../core/models/api.models';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-customers',
  standalone: true,
  imports: [CommonModule, FormsModule, ConfirmDialogComponent],
  templateUrl: './admin-customers.component.html',
  styleUrls: ['./admin-customers.component.scss']})
export class AdminCustomersComponent implements OnInit {
  items: CustomersResponseDTO[] = []; filtered: CustomersResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false; showTransactions = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: CustomersRequestDTO = { firstName: '', lastName: '', email: '', phoneNumber: '', addressId: 1 };
  txSummary: any = null;

  constructor(private svc: CustomerService, private toast: ToastService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(c => (c.firstName + ' ' + c.lastName).toLowerCase().includes(t) || c.email.toLowerCase().includes(t)); }
  edit(c: CustomersResponseDTO): void {
    this.editingId = c.customerId;
    this.form = { firstName: c.firstName, lastName: c.lastName, email: c.email, phoneNumber: c.phoneNumber, addressId: c.address?.addressId || 1 };
    this.showModal = true;
  }
  save(): void {
    if (!this.editingId) return;
    this.svc.update(this.editingId, this.form).subscribe({ next: () => { this.toast.success('Updated!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(c: CustomersResponseDTO): void { this.deleteId = c.customerId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }
  viewTransactions(c: CustomersResponseDTO): void {
    this.svc.getTransactionSummary(c.customerId).subscribe({ next: d => { this.txSummary = d; this.showTransactions = true; }, error: () => { this.txSummary = null; this.showTransactions = true; } });
  }
}
