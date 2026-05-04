import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { CustomerService } from '../../../core/services/customer.service';
import { EmployeeService } from '../../../core/services/employee.service';
import { SupplierService } from '../../../core/services/supplier.service';
import { ToastService } from '../../../core/services/toast.service';
import {
  CustomersResponseDTO,
  CustomerTransactionSummaryDTO,
  EmployeeResponseDTO,
  SupplierResponseDTO,
  UserInfo
} from '../../../core/models/api.models';

type Profile = CustomersResponseDTO | EmployeeResponseDTO | SupplierResponseDTO | null;

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss']
})
export class UserDashboardComponent implements OnInit {
  user: UserInfo | null = null;
  profile: Profile = null;
  transactions: CustomerTransactionSummaryDTO | null = null;
  transactionsLoading = false;
  loading = true;

  constructor(
    private authService: AuthService,
    private customerService: CustomerService,
    private employeeService: EmployeeService,
    private supplierService: SupplierService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.authService.getMe().subscribe({
      next: user => {
        this.user = user;
        this.loadProfile(user);
      },
      error: e => {
        this.loading = false;
        this.toast.handleHttpError(e);
      }
    });
  }

  loadProfile(user: UserInfo): void {
    if (!user.profileId) {
      this.loading = false;
      return;
    }

    if (user.role === 'ROLE_CUSTOMER') {
      this.customerService.getById(user.profileId).subscribe({
        next: profile => {
          this.finish(profile);
          this.loadTransactions(user.profileId!);
        },
        error: e => this.fail(e)
      });
      return;
    }

    if (user.role === 'ROLE_EMPLOYEE') {
      this.employeeService.getById(user.profileId).subscribe({
        next: profile => this.finish(profile),
        error: e => this.fail(e)
      });
      return;
    }

    if (user.role === 'ROLE_SUPPLIER') {
      this.supplierService.getById(user.profileId).subscribe({
        next: profile => this.finish(profile),
        error: e => this.fail(e)
      });
      return;
    }

    this.loading = false;
  }

  finish(profile: Profile): void {
    this.profile = profile;
    this.loading = false;
  }

  fail(error: any): void {
    this.loading = false;
    this.toast.handleHttpError(error);
  }

  loadTransactions(customerId: number): void {
    this.transactionsLoading = true;
    this.customerService.getTransactionSummary(customerId).subscribe({
      next: summary => {
        this.transactions = summary;
        this.transactionsLoading = false;
      },
      error: e => {
        this.transactionsLoading = false;
        this.toast.handleHttpError(e);
      }
    });
  }

  roleLabel(): string {
    return this.user?.role?.replace('ROLE_', '') || 'USER';
  }

  displayName(): string {
    if (!this.profile) return this.user?.username || 'User';

    if ('firstName' in this.profile && 'lastName' in this.profile) {
      return `${this.profile.firstName} ${this.profile.lastName}`;
    }

    if ('name' in this.profile) {
      return this.profile.name;
    }

    return this.user?.username || 'User';
  }
}
