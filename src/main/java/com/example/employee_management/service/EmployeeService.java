package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateEmployeeEmailException;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        String email = normalizeEmail(request.getEmail());
        ensureEmailAvailable(email, null);

        Employee employee = new Employee();
        copyRequestToEmployee(request, employee);
        employee.setEmail(email);

        Employee savedEmployee = employeeRepository.save(employee);
        return toResponse(savedEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        return toResponse(findEmployeeById(id));
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = findEmployeeById(id);
        String email = normalizeEmail(request.getEmail());
        ensureEmailAvailable(email, id);

        copyRequestToEmployee(request, employee);
        employee.setEmail(email);

        Employee updatedEmployee = employeeRepository.save(employee);
        return toResponse(updatedEmployee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeById(id);
        employeeRepository.delete(employee);
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    private void ensureEmailAvailable(String email, Long currentEmployeeId) {
        boolean alreadyExists;

        if (currentEmployeeId == null) {
            alreadyExists = employeeRepository.existsByEmailIgnoreCase(email);
        } else {
            alreadyExists = employeeRepository.existsByEmailIgnoreCaseAndIdNot(email, currentEmployeeId);
        }

        if (alreadyExists) {
            throw new DuplicateEmployeeEmailException(email);
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }

    private void copyRequestToEmployee(EmployeeRequest request, Employee employee) {
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setDepartment(request.getDepartment().trim());
        employee.setSalary(request.getSalary());
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }
}
