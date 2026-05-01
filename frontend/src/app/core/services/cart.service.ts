import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { GroomingResponseDTO, PetFoodResponseDTO, PetResponseDTO, VaccinationResponseDTO } from '../models/api.models';

export type CartItemType = 'pet' | 'food' | 'vaccination' | 'grooming';

export interface CartItem {
  id: string;
  itemType: CartItemType;
  itemId: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly storageKey = 'petshop_cart_items';
  private readonly itemsSubject = new BehaviorSubject<CartItem[]>(this.loadInitialItems());
  readonly items$ = this.itemsSubject.asObservable();

  getItems(): CartItem[] {
    return this.itemsSubject.value;
  }

  getTotalAmount(): number {
    return this.getItems().reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

  getTotalItemsCount(): number {
    return this.getItems().reduce((sum, item) => sum + item.quantity, 0);
  }

  addPet(pet: PetResponseDTO): void {
    this.addOrIncrease({
      id: `pet-${pet.petId}`,
      itemType: 'pet',
      itemId: pet.petId,
      name: pet.name,
      description: `${pet.breed} • ${pet.category?.name ?? 'Pet'}`,
      price: pet.price,
      quantity: 1
    });
  }

  addFood(food: PetFoodResponseDTO): void {
    this.addOrIncrease({
      id: `food-${food.foodId}`,
      itemType: 'food',
      itemId: food.foodId,
      name: food.name,
      description: `${food.brand} • ${food.type}`,
      price: food.price,
      quantity: 1
    });
  }

  addVaccination(vaccination: VaccinationResponseDTO): void {
    this.addOrIncrease({
      id: `vaccination-${vaccination.vaccinationId}`,
      itemType: 'vaccination',
      itemId: vaccination.vaccinationId,
      name: vaccination.name,
      description: vaccination.description,
      price: vaccination.price,
      quantity: 1
    });
  }

  addGrooming(service: GroomingResponseDTO): void {
    this.addOrIncrease({
      id: `grooming-${service.serviceId}`,
      itemType: 'grooming',
      itemId: service.serviceId,
      name: service.name,
      description: service.description,
      price: service.price,
      quantity: 1
    });
  }

  increase(itemId: string): void {
    this.updateQuantity(itemId, 1);
  }

  decrease(itemId: string): void {
    this.updateQuantity(itemId, -1);
  }

  remove(itemId: string): void {
    this.persist(this.getItems().filter(item => item.id !== itemId));
  }

  clear(): void {
    this.persist([]);
  }

  private addOrIncrease(item: CartItem): void {
    const items = this.getItems();
    const existing = items.find(cartItem => cartItem.id === item.id);

    if (existing) {
      this.persist(items.map(cartItem => cartItem.id === item.id
        ? { ...cartItem, quantity: cartItem.quantity + 1 }
        : cartItem
      ));
      return;
    }

    this.persist([...items, item]);
  }

  private updateQuantity(itemId: string, delta: number): void {
    const nextItems = this.getItems()
      .map(item => item.id === itemId ? { ...item, quantity: item.quantity + delta } : item)
      .filter(item => item.quantity > 0);

    this.persist(nextItems);
  }

  private loadInitialItems(): CartItem[] {
    const serialized = localStorage.getItem(this.storageKey);
    if (!serialized) {
      return [];
    }

    try {
      const parsed = JSON.parse(serialized);
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  private persist(items: CartItem[]): void {
    this.itemsSubject.next(items);
    localStorage.setItem(this.storageKey, JSON.stringify(items));
  }
}
