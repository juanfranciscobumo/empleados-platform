const API_URL = window.location.hostname === 'localhost' 
    ? 'http://localhost:8081' 
    : 'http://ec2-3-83-67-172.compute-1.amazonaws.com:8081';

const employeeForm = document.getElementById('employeeForm');
const employeeTableBody = document.getElementById('employeeTableBody');
const formTitle = document.getElementById('formTitle');
const submitBtn = document.getElementById('submitBtn');
const cancelBtn = document.getElementById('cancelBtn');
const employeeIdInput = document.getElementById('employeeId');
const nameInput = document.getElementById('name');
const roleInput = document.getElementById('role');
const apiStatus = document.getElementById('apiStatus');
const stats = document.getElementById('stats');
const toast = document.getElementById('toast');

let isEditing = false;

document.addEventListener('DOMContentLoaded', () => {
    checkApiStatus();
    loadEmployees();
    setupEventListeners();
});

function setupEventListeners() {
    employeeForm.addEventListener('submit', handleSubmit);
    cancelBtn.addEventListener('click', resetForm);
}

async function checkApiStatus() {
    const statusDot = apiStatus.querySelector('.status-dot');
    const statusText = apiStatus.querySelector('.status-text');
    
    try {
        const response = await fetch(`${API_URL}/employees`, { 
            method: 'GET',
            signal: AbortSignal.timeout(5000)
        });
        
        if (response.ok) {
            statusDot.className = 'status-dot online';
            statusText.textContent = 'API Conectada';
        } else {
            throw new Error('API no disponible');
        }
    } catch (error) {
        statusDot.className = 'status-dot offline';
        statusText.textContent = 'API No Disponible';
    }
}

async function loadEmployees() {
    try {
        const response = await fetch(`${API_URL}/employees`);
        
        if (!response.ok) throw new Error('Error al cargar empleados');
        
        const employees = await response.json();
        renderEmployees(employees);
        updateStats(employees.length);
    } catch (error) {
        showToast('Error al conectar con la API', 'error');
        employeeTableBody.innerHTML = `
            <tr>
                <td colspan="4" class="loading">
                    <i class="fas fa-exclamation-circle"></i> 
                    No se pudo conectar a la API. Verifica que esté corriendo.
                </td>
            </tr>
        `;
    }
}

function renderEmployees(employees) {
    if (employees.length === 0) {
        employeeTableBody.innerHTML = `
            <tr>
                <td colspan="4" class="loading">
                    <i class="fas fa-inbox"></i> 
                    No hay empleados registrados
                </td>
            </tr>
        `;
        return;
    }

    employeeTableBody.innerHTML = employees.map(employee => `
        <tr data-id="${employee.id}">
            <td><strong>#${employee.id}</strong></td>
            <td>${escapeHtml(employee.name)}</td>
            <td>${escapeHtml(employee.role)}</td>
            <td class="actions">
                <button class="btn btn-edit" onclick="editEmployee(${employee.id}, '${escapeHtml(employee.name)}', '${escapeHtml(employee.role)}')">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-danger" onclick="deleteEmployee(${employee.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

function updateStats(count) {
    stats.textContent = `Total: ${count} empleado${count !== 1 ? 's' : ''}`;
}

async function handleSubmit(e) {
    e.preventDefault();
    
    const employeeData = {
        name: nameInput.value.trim(),
        role: roleInput.value.trim()
    };

    try {
        let response;
        
        if (isEditing) {
            response = await fetch(`${API_URL}/employees/${employeeIdInput.value}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(employeeData)
            });
        } else {
            response = await fetch(`${API_URL}/employees`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(employeeData)
            });
        }

        if (response.ok) {
            showToast(isEditing ? 'Empleado actualizado' : 'Empleado creado', 'success');
            resetForm();
            loadEmployees();
        } else {
            const error = await response.json();
            throw new Error(error.message || 'Error al guardar');
        }
    } catch (error) {
        showToast(error.message || 'Error al guardar el empleado', 'error');
    }
}

function editEmployee(id, name, role) {
    isEditing = true;
    employeeIdInput.value = id;
    nameInput.value = name;
    roleInput.value = role;
    formTitle.textContent = 'Editar Empleado';
    submitBtn.innerHTML = '<i class="fas fa-save"></i> Actualizar';
    cancelBtn.style.display = 'inline-flex';
    nameInput.focus();
}

async function deleteEmployee(id) {
    if (!confirm('¿Estás seguro de eliminar este empleado?')) return;

    try {
        const response = await fetch(`${API_URL}/employees/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast('Empleado eliminado', 'success');
            loadEmployees();
        } else {
            throw new Error('Error al eliminar');
        }
    } catch (error) {
        showToast('Error al eliminar el empleado', 'error');
    }
}

function resetForm() {
    isEditing = false;
    employeeForm.reset();
    employeeIdInput.value = '';
    formTitle.textContent = 'Agregar Empleado';
    submitBtn.innerHTML = '<i class="fas fa-save"></i> Guardar';
    cancelBtn.style.display = 'none';
}

function showToast(message, type = 'success') {
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.className = 'toast';
    }, 3000);
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
