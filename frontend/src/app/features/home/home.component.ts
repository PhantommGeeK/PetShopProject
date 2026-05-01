import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PetService } from '../../core/services/pet.service';
import { PetCategoryService } from '../../core/services/pet-category.service';
import { PetResponseDTO, PetCategoryResponseDTO } from '../../core/models/api.models';
import { PetCardComponent } from '../../shared/components/pet-card/pet-card.component';
import { CurrencyInrPipe } from '../../shared/pipes/currency-inr.pipe';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, PetCardComponent, CurrencyInrPipe],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']})
export class HomeComponent implements OnInit {
  categories: PetCategoryResponseDTO[] = [];
  bestSellers: PetResponseDTO[] = [];
  featuredPet: PetResponseDTO | null = null;
  loadingCategories = true;
  loadingPets = true;
  showAllCategories = false;

  get displayedCategories(): PetCategoryResponseDTO[] {
    return this.showAllCategories ? this.categories : this.categories.slice(0, 4);
  }

  private categoryEmojis: Record<string, string> = {
    'dog': '🐕', 'dogs': '🐕',
    'cat': '🐈', 'cats': '🐈',
    'bird': '🦜', 'birds': '🦜',
    'fish': '🐠', 'fishes': '🐠',
    'rabbit': '🐇', 'rabbits': '🐇',
    'hamster': '🐹', 'hamsters': '🐹',
    'reptile': '🦎', 'reptiles': '🦎',
    'turtle': '🐢', 'turtles': '🐢',
  };

  constructor(
    private petService: PetService,
    private categoryService: PetCategoryService
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadPets();
  }

  loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (data) => {
        const uniqueByKey = new Map<string, PetCategoryResponseDTO>();
        for (const category of data) {
          const key = category.name.toLowerCase().trim();

          if (!uniqueByKey.has(key)) {
            uniqueByKey.set(key, category);
          }
        }
        this.categories = Array.from(uniqueByKey.values());
        this.loadingCategories = false;
      },
      error: () => {
        this.loadingCategories = false;
      }
    });
  }

  loadPets(): void {
    this.petService.getAll().subscribe({
      next: (data) => {
        this.bestSellers = data.slice(0, 4);
        this.featuredPet = data.length > 0 ? data[0] : null;
        this.loadingPets = false;
      },
      error: () => {
        this.loadingPets = false;
      }
    });
  }

  getCategoryEmoji(name: string): string {
    const key = name.toLowerCase().trim();
    return this.categoryEmojis[key] || '🐾';
  }
}
