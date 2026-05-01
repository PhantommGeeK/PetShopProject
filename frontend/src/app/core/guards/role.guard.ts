import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const allowedRoles: string[] = route.data?.['roles'] || [];
  const userRole = authService.getRole();

  if (allowedRoles.length === 0 || allowedRoles.includes(userRole)) {
    return true;
  }

  router.navigate(['/']);
  return false;
};
