package com.cg.dto;

import java.time.LocalDate;

public class EmployeeResponseDTO {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private LocalDate hireDate;
    private String phoneNumber;
    private String email;
    private AdressesResponseDTO address;

    public EmployeeResponseDTO() {}

    public EmployeeResponseDTO(int employeeId, String firstName, String lastName,
                                String position, LocalDate hireDate, String phoneNumber,
                                String email, AdressesResponseDTO address ) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public AdressesResponseDTO getAddress() { return address; }
    public void setAddress(AdressesResponseDTO address) { this.address = address; }
}