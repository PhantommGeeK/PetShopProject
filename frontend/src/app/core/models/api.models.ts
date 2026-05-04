// Address
export interface AddressesResponseDTO {
  addressId: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
}

// Pet Category
export interface PetCategoryResponseDTO {
  categoryId: number;
  name: string;
}

// Pet models - backend returns nested category object, not categoryId
export interface PetResponseDTO {
  petId: number;
  name: string;
  breed: string;
  age: number;
  price: number;
  description: string;
  imageUrl: string;
  category: PetCategoryResponseDTO;
}

export interface PetRequestDTO {
  name: string;
  breed: string;
  age: number;
  price: number;
  description: string;
  imageUrl: string;
  categoryId: number;
}

// Pet Food
export interface PetFoodResponseDTO {
  foodId: number;
  name: string;
  brand: string;
  type: string;
  quantity: number;
  price: number;
}

export interface PetFoodRequestDTO {
  name: string;
  brand: string;
  type: string;
  quantity: number;
  price: number;
}

// Grooming
export interface GroomingResponseDTO {
  serviceId: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
}

export interface GroomingRequestDTO {
  name: string;
  description: string;
  price: number;
  available: boolean;
}

// Vaccination
export interface VaccinationResponseDTO {
  vaccinationId: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
}

export interface VaccinationRequestDTO {
  name: string;
  description: string;
  price: number;
  available: boolean;
}

// Customer - backend returns nested address object
export interface CustomersResponseDTO {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: AddressesResponseDTO;
}

export interface CustomersRequestDTO {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  addressId: number;
}

export interface CustomerTransactionSummaryDTO {
  customerId: number;
  firstName: string;
  lastName: string;
  transactionIds: number[];
  totalTransactions: number;
  successfulPurchases: number;
  totalAmount: number;
  transactions: TransactionResponseDTO[];
}

// Employee - backend returns nested address object
export interface EmployeeResponseDTO {
  employeeId: number;
  firstName: string;
  lastName: string;
  position: string;
  hireDate: string;
  phoneNumber: string;
  email: string;
  address: AddressesResponseDTO;
}

export interface EmployeeRequestDTO {
  firstName: string;
  lastName: string;
  position: string;
  hireDate: string;
  phoneNumber: string;
  email: string;
  addressId: number;
}

// Supplier - backend returns nested address object
export interface SupplierResponseDTO {
  supplierId: number;
  name: string;
  contactPerson: string;
  phoneNumber: string;
  email: string;
  address: AddressesResponseDTO;
}

export interface SupplierRequestDTO {
  name: string;
  contactPerson: string;
  phoneNumber: string;
  email: string;
  addressId?: number | null;
  address?: AddressesRequestDTO | null;
}

// Transaction - backend returns nested objects
export interface TransactionResponseDTO {
  transactionId: number;
  transactionDate: string;
  amount: number;
  transactionStatus: string;
  itemType?: string;
  itemName?: string;
  quantity?: number;
  customers: CustomersResponseDTO;
  pet?: PetResponseDTO | null;
}

export interface TransactionRequestDTO {
  transactionDate: string;
  amount: number;
  transactionStatus: string;
  itemType: string;
  itemName: string;
  quantity: number;
  customerId: number;
  petId?: number | null;
  foodId?: number | null;
  groomingServiceId?: number | null;
  vaccinationId?: number | null;
}

// Auth
export interface LoginRequestDTO {
  username: string;
  password: string;
}

export interface LoginResponseDTO {
  token: string;
}

export interface SuccessDTO {
  message: string;
}

export interface UserInfo {
  userId?: number;
  profileId?: number;
  username: string;
  role: string;
}

// Registration DTOs - backend expects nested address object
export interface AddressesRequestDTO {
  street: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface CustomerRegisterDTO {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  username: string;
  password: string;
  address: AddressesRequestDTO;
}

export interface EmployeeRegisterDTO {
  firstName: string;
  lastName: string;
  position: string;
  hireDate: string;
  phoneNumber: string;
  email: string;
  username: string;
  password: string;
  address: AddressesRequestDTO;
}

export interface SupplierRegisterDTO {
  name: string;
  contactPerson: string;
  phoneNumber: string;
  email: string;
  username: string;
  password: string;
  address: AddressesRequestDTO;
}
