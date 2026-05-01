import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']})
export class RegisterComponent {
  activeTab: 'customer' | 'supplier' | 'employee' = 'customer';
  loading = false;

  cust: any = {
    firstName: '', lastName: '', email: '', phoneNumber: '',
    username: '', password: '',
    address: { street: '', city: '', state: '', zipCode: '' }
  };

  supp: any = {
    name: '', contactPerson: '', phoneNumber: '', email: '',
    username: '', password: '',
    address: { street: '', city: '', state: '', zipCode: '' }
  };

  emp: any = {
    firstName: '', lastName: '', position: '', hireDate: '',
    phoneNumber: '', email: '', username: '', password: '',
    address: { street: '', city: '', state: '', zipCode: '' }
  };

  constructor(private authService: AuthService, private toast: ToastService, private router: Router) {}

  registerCustomer(): void {
    this.loading = true;
    this.authService.registerCustomer(this.cust).subscribe({
      next: () => { this.toast.success('Registration successful! Please login.'); this.router.navigate(['/login']); this.loading = false; },
      error: (e) => { this.loading = false; this.toast.handleHttpError(e); }
    });
  }

  registerSupplier(): void {
    this.loading = true;
    this.authService.registerSupplier(this.supp).subscribe({
      next: () => { this.toast.success('Registration successful! Please login.'); this.router.navigate(['/login']); this.loading = false; },
      error: (e) => { this.loading = false; this.toast.handleHttpError(e); }
    });
  }

  registerEmployee(): void {
    this.loading = true;
    this.authService.registerEmployee(this.emp).subscribe({
      next: () => { this.toast.success('Registration successful! Please login.'); this.router.navigate(['/login']); this.loading = false; },
      error: (e) => { this.loading = false; this.toast.handleHttpError(e); }
    });
  }
}
