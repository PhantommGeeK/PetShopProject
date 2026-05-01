import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PetService } from '../../../core/services/pet.service';
import { PetCategoryService } from '../../../core/services/pet-category.service';
import { PetResponseDTO, PetCategoryResponseDTO } from '../../../core/models/api.models';
import { PetCardComponent } from '../../../shared/components/pet-card/pet-card.component';
import { Subject, debounceTime } from 'rxjs';

@Component({
  selector: 'app-pet-list',
  standalone: true,
  imports: [CommonModule, FormsModule, PetCardComponent],
  templateUrl: './pet-list.component.html',
  styleUrls: ['./pet-list.component.scss']})
export class PetListComponent implements OnInit {
  pets: PetResponseDTO[] = [];
  categories: PetCategoryResponseDTO[] = [];
  loading = true;
  searchName = ''; searchBreed = '';
  selectedCategory = 0;
  minPrice: number | null = null; maxPrice: number | null = null;
  private searchSubject = new Subject<void>();

  constructor(private petService: PetService, private catService: PetCategoryService, private route: ActivatedRoute) {
    this.searchSubject.pipe(debounceTime(400)).subscribe(() => this.applyFilters());
  }

  ngOnInit(): void {
    this.catService.getAll().subscribe(c => this.categories = c);
    this.route.queryParams.subscribe(p => {
      if (p['category']) { this.selectedCategory = +p['category']; }
      this.applyFilters();
    });
  }

  onSearchChange(): void { this.searchSubject.next(); }
  onBreedChange(): void { this.searchSubject.next(); }
  onPriceChange(): void { this.searchSubject.next(); }
  onCategoryChange(): void { this.applyFilters(); }

  applyFilters(): void {
    this.loading = true;
    if (this.searchName.trim()) {
      this.petService.searchByName(this.searchName).subscribe({ next: d => { this.pets = d; this.loading = false; }, error: () => this.loading = false });
    } else if (this.searchBreed.trim()) {
      this.petService.searchByBreed(this.searchBreed).subscribe({ next: d => { this.pets = d; this.loading = false; }, error: () => this.loading = false });
    } else if (this.minPrice && this.maxPrice) {
      this.petService.searchByPriceRange(this.minPrice, this.maxPrice).subscribe({ next: d => { this.pets = d; this.loading = false; }, error: () => this.loading = false });
    } else if (this.selectedCategory > 0) {
      this.petService.getByCategory(this.selectedCategory).subscribe({ next: d => { this.pets = d; this.loading = false; }, error: () => this.loading = false });
    } else {
      this.petService.getAll().subscribe({ next: d => { this.pets = d; this.loading = false; }, error: () => this.loading = false });
    }
  }
}
