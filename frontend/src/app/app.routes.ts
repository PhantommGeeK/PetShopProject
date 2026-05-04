import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'pets',
    loadComponent: () => import('./features/pets/pet-list/pet-list.component').then(m => m.PetListComponent)
  },
  {
    path: 'pets/:id',
    loadComponent: () => import('./features/pets/pet-detail/pet-detail.component').then(m => m.PetDetailComponent)
  },
  {
    path: 'pet-food',
    loadComponent: () => import('./features/pet-food/pet-food.component').then(m => m.PetFoodComponent)
  },
  {
    path: 'grooming',
    loadComponent: () => import('./features/grooming/grooming.component').then(m => m.GroomingComponent)
  },
  {
    path: 'vaccinations',
    loadComponent: () => import('./features/vaccinations/vaccinations.component').then(m => m.VaccinationsComponent)
  },
  {
    path: 'cart',
    loadComponent: () => import('./features/cart/cart.component').then(m => m.CartComponent)
  },
  {
    path: 'order',
    loadComponent: () => import('./features/order/order.component').then(m => m.OrderComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/user/user-dashboard/user-dashboard.component').then(m => m.UserDashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'supplier/dashboard',
    loadComponent: () => import('./features/supplier/supplier-dashboard/supplier-dashboard.component').then(m => m.SupplierDashboardComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_SUPPLIER'] }
  },
  // Admin routes
  {
    path: 'admin/pets',
    loadComponent: () => import('./features/admin/admin-pets/admin-pets.component').then(m => m.AdminPetsComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_EMPLOYEE'] }
  },
  {
    path: 'admin/pet-categories',
    loadComponent: () => import('./features/admin/admin-categories/admin-categories.component').then(m => m.AdminCategoriesComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN'] }
  },
  {
    path: 'admin/pet-food',
    loadComponent: () => import('./features/admin/admin-pet-food/admin-pet-food.component').then(m => m.AdminPetFoodComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_EMPLOYEE'] }
  },
  {
    path: 'admin/grooming',
    loadComponent: () => import('./features/admin/admin-grooming/admin-grooming.component').then(m => m.AdminGroomingComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_EMPLOYEE'] }
  },
  {
    path: 'admin/vaccinations',
    loadComponent: () => import('./features/admin/admin-vaccinations/admin-vaccinations.component').then(m => m.AdminVaccinationsComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_EMPLOYEE'] }
  },
  {
    path: 'admin/customers',
    loadComponent: () => import('./features/admin/admin-customers/admin-customers.component').then(m => m.AdminCustomersComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN'] }
  },
  {
    path: 'admin/employees',
    loadComponent: () => import('./features/admin/admin-employees/admin-employees.component').then(m => m.AdminEmployeesComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN'] }
  },
  {
    path: 'admin/suppliers',
    loadComponent: () => import('./features/admin/admin-suppliers/admin-suppliers.component').then(m => m.AdminSuppliersComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_EMPLOYEE'] }
  },
  {
    path: 'admin/transactions',
    loadComponent: () => import('./features/admin/admin-transactions/admin-transactions.component').then(m => m.AdminTransactionsComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_EMPLOYEE'] }
  },
  {
    path: '**',
    redirectTo: ''
  }
];
