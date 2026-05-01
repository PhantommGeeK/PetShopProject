import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GroomingService } from '../../core/services/grooming.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { CartService } from '../../core/services/cart.service';
import { GroomingResponseDTO, GroomingRequestDTO } from '../../core/models/api.models';
import { CurrencyInrPipe } from '../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-grooming',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './grooming.component.html',
  styleUrls: ['./grooming.component.scss']})
export class GroomingComponent implements OnInit {
  services: GroomingResponseDTO[] = [];
  availableOnly = false; loading = true;
  showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: GroomingRequestDTO = { name: '', description: '', price: 0, available: true };

  constructor(
    public authService: AuthService,
    private groomService: GroomingService,
    private cartService: CartService,
    private toast: ToastService
  ) {}
  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    const obs = this.availableOnly ? this.groomService.getAvailable() : this.groomService.getAll();
    obs.subscribe({ next: d => { this.services = d; this.loading = false; }, error: () => this.loading = false });
  }

  toggleFilter(avail: boolean): void { this.availableOnly = avail; this.load(); }
  openModal(s?: GroomingResponseDTO): void {
    if (s) { this.editingId = s.serviceId; this.form = { name: s.name, description: s.description, price: s.price, available: s.available }; }
    else { this.editingId = null; this.form = { name: '', description: '', price: 0, available: true }; }
    this.showModal = true;
  }
  editService(s: GroomingResponseDTO): void { this.openModal(s); }
  save(): void {
    const obs = this.editingId ? this.groomService.update(this.editingId, this.form) : this.groomService.create(this.form);
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(s: GroomingResponseDTO): void { this.deleteId = s.serviceId; this.showConfirm = true; }

  addToCart(service: GroomingResponseDTO): void {
    if (!service.available) {
      return;
    }
    this.cartService.addGrooming(service);
    this.toast.success(`${service.name} added to cart`);
  }

  deleteService(): void {
    if (!this.deleteId) return;
    this.groomService.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
}
