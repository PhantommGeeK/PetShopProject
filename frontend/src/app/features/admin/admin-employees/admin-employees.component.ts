import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../../core/services/employee.service';
import { ToastService } from '../../../core/services/toast.service';
import { EmployeeResponseDTO, EmployeeRequestDTO } from '../../../core/models/api.models';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-admin-employees',
  standalone: true,
  imports: [CommonModule, FormsModule, ConfirmDialogComponent],
  templateUrl: './admin-employees.component.html',})
export class AdminEmployeesComponent implements OnInit {
  items: EmployeeResponseDTO[] = []; filtered: EmployeeResponseDTO[] = [];
  searchTerm = ''; showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: EmployeeRequestDTO = { firstName: '', lastName: '', position: '', hireDate: '', phoneNumber: '', email: '', addressId: 1 };

  constructor(private svc: EmployeeService, private toast: ToastService) {}
  ngOnInit(): void { this.load(); }
  load(): void { this.svc.getAll().subscribe({ next: d => { this.items = d; this.filter(); }, error: e => this.toast.handleHttpError(e) }); }
  filter(): void { const t = this.searchTerm.toLowerCase(); this.filtered = this.items.filter(e => (e.firstName + ' ' + e.lastName).toLowerCase().includes(t) || e.position.toLowerCase().includes(t)); }
  edit(e: EmployeeResponseDTO): void {
    this.editingId = e.employeeId;
    this.form = { firstName: e.firstName, lastName: e.lastName, position: e.position, hireDate: e.hireDate, phoneNumber: e.phoneNumber, email: e.email, addressId: e.address?.addressId || 1 };
    this.showModal = true;
  }
  save(): void {
    if (!this.editingId) return;
    this.svc.update(this.editingId, this.form).subscribe({ next: () => { this.toast.success('Updated!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(e: EmployeeResponseDTO): void { this.deleteId = e.employeeId; this.showConfirm = true; }
  deleteItem(): void { if (!this.deleteId) return; this.svc.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) }); }
}
