import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']})
export class LoginComponent {
  username = ''; password = ''; showPassword = false; loading = false; errorMessage = '';

  constructor(private authService: AuthService, private toast: ToastService, private router: Router) {}

  onLogin(): void {
    if (!this.username || !this.password) { this.errorMessage = 'Please enter both username and password.'; return; }
    this.loading = true; this.errorMessage = '';
    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.toast.success('Welcome back!');
        const role = this.authService.getRole();
        if (role === 'ROLE_CUSTOMER') this.router.navigate(['/dashboard']);
        else if (role === 'ROLE_ADMIN') this.router.navigate(['/admin/pets']);
        else if (role === 'ROLE_EMPLOYEE') this.router.navigate(['/admin/pets']);
        else if (role === 'ROLE_SUPPLIER') this.router.navigate(['/supplier/dashboard']);
        else this.router.navigate(['/']);
        this.loading = false;
      },
      error: (err) => { this.loading = false; this.errorMessage = err.status === 401 ? 'Invalid username or password.' : 'Something went wrong.'; }
    });
  }
}
