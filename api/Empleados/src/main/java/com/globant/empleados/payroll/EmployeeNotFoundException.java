package com.globant.empleados.payroll;

class EmployeeNotFoundException extends RuntimeException {

    EmployeeNotFoundException(Long id) {
        super("No se encontró el nombre del empleado con id: " + id);
    }
}

