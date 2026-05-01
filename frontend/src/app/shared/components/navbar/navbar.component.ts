import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']})
export class NavbarComponent {
  isScrolled = false;
  mobileOpen = false;
  userMenuOpen = false;

  constructor(public authService: AuthService, private router: Router) {}

  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 80;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-menu')) {
      this.userMenuOpen = false;
    }
  }

  toggleMobile(): void {
    this.mobileOpen = !this.mobileOpen;
  }

  closeMobile(): void {
    this.mobileOpen = false;
    this.userMenuOpen = false;
  }

  toggleUserMenu(): void {
    this.userMenuOpen = !this.userMenuOpen;
  }

  getRoleDisplay(): string {
    const role = this.authService.getRole();
    return role.replace('ROLE_', '');
  }

  logout(): void {
    this.authService.logout();
    this.userMenuOpen = false;
    this.router.navigate(['/']);
  }
}
