import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VaccinationService } from '../../core/services/vaccination.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { CartService } from '../../core/services/cart.service';
import { VaccinationResponseDTO, VaccinationRequestDTO } from '../../core/models/api.models';
import { CurrencyInrPipe } from '../../shared/pipes/currency-inr.pipe';
import { ConfirmDialogComponent } from '../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-vaccinations',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyInrPipe, ConfirmDialogComponent],
  templateUrl: './vaccinations.component.html',
  styleUrls: ['./vaccinations.component.scss']})
export class VaccinationsComponent implements OnInit {
  vaccinations: VaccinationResponseDTO[] = [];
  availableOnly = false; loading = true;
  showModal = false; showConfirm = false;
  editingId: number | null = null; deleteId: number | null = null;
  form: VaccinationRequestDTO = { name: '', description: '', price: 0, available: true };

  constructor(
    public authService: AuthService,
    private vaccService: VaccinationService,
    private cartService: CartService,
    private toast: ToastService
  ) {}
  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    const obs = this.availableOnly ? this.vaccService.getAvailable() : this.vaccService.getAll();
    obs.subscribe({ next: d => { this.vaccinations = d; this.loading = false; }, error: () => this.loading = false });
  }

  toggleFilter(avail: boolean): void { this.availableOnly = avail; this.load(); }
  openModal(v?: VaccinationResponseDTO): void {
    if (v) { this.editingId = v.vaccinationId; this.form = { name: v.name, description: v.description, price: v.price, available: v.available }; }
    else { this.editingId = null; this.form = { name: '', description: '', price: 0, available: true }; }
    this.showModal = true;
  }
  editVacc(v: VaccinationResponseDTO): void { this.openModal(v); }
  save(): void {
    const obs = this.editingId ? this.vaccService.update(this.editingId, this.form) : this.vaccService.create(this.form);
    obs.subscribe({ next: () => { this.toast.success('Saved!'); this.showModal = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
  confirmDel(v: VaccinationResponseDTO): void { this.deleteId = v.vaccinationId; this.showConfirm = true; }

  addToCart(vaccination: VaccinationResponseDTO): void {
    if (!vaccination.available) {
      return;
    }
    this.cartService.addVaccination(vaccination);
    this.toast.success(`${vaccination.name} added to cart`);
  }

  deleteVacc(): void {
    if (!this.deleteId) return;
    this.vaccService.delete(this.deleteId).subscribe({ next: () => { this.toast.success('Deleted!'); this.showConfirm = false; this.load(); }, error: e => this.toast.handleHttpError(e) });
  }
}
