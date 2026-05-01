import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PetService } from '../../../core/services/pet.service';
import { PetResponseDTO } from '../../../core/models/api.models';
import { PetCardComponent } from '../../../shared/components/pet-card/pet-card.component';
import { CurrencyInrPipe } from '../../../shared/pipes/currency-inr.pipe';

@Component({
  selector: 'app-pet-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, PetCardComponent, CurrencyInrPipe],
  templateUrl: './pet-detail.component.html',
  styleUrls: ['./pet-detail.component.scss']})
export class PetDetailComponent implements OnInit {
  pet: PetResponseDTO | null = null;
  relatedPets: PetResponseDTO[] = [];
  loading = true;

  constructor(private petService: PetService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      const id = +p['id'];
      this.loading = true;
      this.petService.getById(id).subscribe({
        next: (pet) => { this.pet = pet; this.loading = false; this.loadRelated(pet.category?.categoryId, pet.petId); },
        error: () => this.loading = false
      });
    });
  }

  loadRelated(catId: number, excludeId: number): void {
    this.petService.getByCategory(catId).subscribe({
      next: (pets) => this.relatedPets = pets.filter(p => p.petId !== excludeId).slice(0, 4),
      error: () => {}
    });
  }
}
