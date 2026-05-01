import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  dismissing?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private toasts = new BehaviorSubject<Toast[]>([]);
  toasts$ = this.toasts.asObservable();
  private nextId = 0;

  show(message: string, type: 'success' | 'error' | 'info' | 'warning' = 'info'): void {
    const toast: Toast = { id: this.nextId++, message, type };
    this.toasts.next([...this.toasts.value, toast]);

    setTimeout(() => this.dismiss(toast.id), 3000);
  }

  success(message: string): void {
    this.show(message, 'success');
  }

  error(message: string): void {
    this.show(message, 'error');
  }

  warning(message: string): void {
    this.show(message, 'warning');
  }

  dismiss(id: number): void {
    const current = this.toasts.value;
    const idx = current.findIndex(t => t.id === id);
    if (idx > -1) {
      current[idx].dismissing = true;
      this.toasts.next([...current]);

      setTimeout(() => {
        this.toasts.next(this.toasts.value.filter(t => t.id !== id));
      }, 300);
    }
  }

  handleHttpError(error: any): void {
    if (error.status === 401) {
      this.error('Session expired. Please log in.');
    } else if (error.status === 404) {
      this.error('Resource not found.');
    } else if (error.status === 400) {
      const body = error.error;
      if (body?.fieldErrors && Array.isArray(body.fieldErrors)) {
        body.fieldErrors.forEach((fe: any) => {
          this.error(fe.message || fe);
        });
      } else if (body?.message) {
        this.error(body.message);
      } else {
        this.error('Invalid request. Please check your input.');
      }
    } else if (error.status === 403) {
      this.error('You do not have permission to perform this action.');
    } else {
      this.error('Something went wrong. Try again.');
    }
  }
}
