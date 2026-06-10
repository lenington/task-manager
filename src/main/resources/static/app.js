const apiUrl = '/api/tasks';

const labels = {
	TODO: 'A fazer',
	IN_PROGRESS: 'Em andamento',
	DONE: 'Concluída',
	LOW: 'Baixa',
	MEDIUM: 'Média',
	HIGH: 'Alta'
};

const state = {
	tasks: [],
	editingId: null
};

const form = document.getElementById('task-form');
const taskId = document.getElementById('task-id');
const title = document.getElementById('title');
const description = document.getElementById('description');
const statusField = document.getElementById('status');
const priorityField = document.getElementById('priority');
const submitButton = document.getElementById('submit-button');
const cancelButton = document.getElementById('cancel-button');
const titleError = document.getElementById('title-error');
const descriptionCounter = document.getElementById('description-counter');
const filterStatus = document.getElementById('filter-status');
const filterPriority = document.getElementById('filter-priority');
const taskList = document.getElementById('task-list');
const message = document.getElementById('message');
const totalCount = document.getElementById('total-count');
const doneCount = document.getElementById('done-count');

form.addEventListener('submit', saveTask);
cancelButton.addEventListener('click', resetForm);
description.addEventListener('input', updateDescriptionCounter);
filterStatus.addEventListener('change', loadTasks);
filterPriority.addEventListener('change', loadTasks);

loadTasks();
updateDescriptionCounter();

async function loadTasks() {
	const params = new URLSearchParams();
	if (filterStatus.value) params.set('status', filterStatus.value);
	if (filterPriority.value) params.set('priority', filterPriority.value);

	try {
		setMessage('Carregando tarefas...');
		const response = await fetch(`${apiUrl}${params.toString() ? `?${params}` : ''}`);
		if (!response.ok) throw new Error('Falha ao carregar tarefas');

		state.tasks = await response.json();
		renderTasks();
		setMessage(state.tasks.length ? '' : 'Nenhuma tarefa encontrada.');
	} catch (error) {
		setMessage(error.message, true);
	}
}

async function saveTask(event) {
	event.preventDefault();

	if (!validateForm()) return;

	const payload = {
		title: title.value.trim(),
		description: description.value.trim(),
		status: statusField.value,
		priority: priorityField.value
	};

	const id = taskId.value;
	const method = id ? 'PUT' : 'POST';
	const url = id ? `${apiUrl}/${id}` : apiUrl;

	try {
		submitButton.disabled = true;
		const response = await fetch(url, {
			method,
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(payload)
		});

		if (!response.ok) {
			const error = await response.json();
			throw new Error(error.message || 'Não foi possível salvar a tarefa');
		}

		resetForm();
		await loadTasks();
		setMessage(id ? 'Tarefa atualizada.' : 'Tarefa criada.');
	} catch (error) {
		setMessage(error.message, true);
	} finally {
		submitButton.disabled = false;
	}
}

async function removeTask(id) {
	const task = state.tasks.find(item => item.id === id);
	if (!window.confirm(`Remover "${task.title}"?`)) return;

	try {
		const response = await fetch(`${apiUrl}/${id}`, { method: 'DELETE' });
		if (!response.ok) throw new Error('Não foi possível remover a tarefa');

		await loadTasks();
		setMessage('Tarefa removida.');
	} catch (error) {
		setMessage(error.message, true);
	}
}

function editTask(id) {
	const task = state.tasks.find(item => item.id === id);
	if (!task) return;

	state.editingId = id;
	taskId.value = task.id;
	title.value = task.title;
	description.value = task.description || '';
	statusField.value = task.status;
	priorityField.value = task.priority;
	submitButton.textContent = 'Atualizar';
	document.getElementById('form-title').textContent = 'Editar tarefa';
	title.focus();
	updateDescriptionCounter();
}

function renderTasks() {
	taskList.innerHTML = '';

	state.tasks.forEach(task => {
		const card = document.createElement('article');
		card.className = 'task-card';
		card.innerHTML = `
			<header>
				<h3>${escapeHtml(task.title)}</h3>
			</header>
			<p>${escapeHtml(task.description || 'Sem descrição')}</p>
			<div class="badges">
				<span class="badge ${task.status === 'DONE' ? 'done' : task.status === 'IN_PROGRESS' ? 'progress' : ''}">${labels[task.status]}</span>
				<span class="badge ${task.priority.toLowerCase()}">${labels[task.priority]}</span>
			</div>
			<div class="task-actions">
				<button type="button" class="secondary" data-action="edit" data-id="${task.id}">Editar</button>
				<button type="button" class="danger" data-action="delete" data-id="${task.id}">Remover</button>
			</div>
		`;
		taskList.appendChild(card);
	});

	taskList.querySelectorAll('button[data-action="edit"]').forEach(button => {
		button.addEventListener('click', () => editTask(Number(button.dataset.id)));
	});
	taskList.querySelectorAll('button[data-action="delete"]').forEach(button => {
		button.addEventListener('click', () => removeTask(Number(button.dataset.id)));
	});

	totalCount.textContent = state.tasks.length;
	doneCount.textContent = state.tasks.filter(task => task.status === 'DONE').length;
}

function validateForm() {
	titleError.textContent = '';

	if (!title.value.trim()) {
		titleError.textContent = 'Título é obrigatório';
		title.focus();
		return false;
	}

	return true;
}

function resetForm() {
	state.editingId = null;
	form.reset();
	taskId.value = '';
	statusField.value = 'TODO';
	priorityField.value = 'MEDIUM';
	submitButton.textContent = 'Salvar';
	document.getElementById('form-title').textContent = 'Nova tarefa';
	titleError.textContent = '';
	updateDescriptionCounter();
}

function updateDescriptionCounter() {
	descriptionCounter.textContent = `${description.value.length}/500`;
}

function setMessage(text, isError = false) {
	message.textContent = text;
	message.style.color = isError ? '#b42318' : '#69707a';
}

function escapeHtml(value) {
	return String(value)
		.replace(/&/g, '&amp;')
		.replace(/</g, '&lt;')
		.replace(/>/g, '&gt;')
		.replace(/"/g, '&quot;')
		.replace(/'/g, '&#039;');
}
