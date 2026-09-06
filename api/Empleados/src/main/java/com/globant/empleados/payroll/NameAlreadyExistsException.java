package com.globant.empleados.payroll;

class NameAlreadyExistsException extends RuntimeException {

    NameAlreadyExistsException(String name) {
        super("El nombre de empleado " + name + " ya se encuentra registrado");
    }
}

