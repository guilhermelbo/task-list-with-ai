import {
    TaskResource,
    CreateTaskRequest,
    UpdateTaskRequest,
    PageResult,
    ErrorResponse
} from '@/types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

class ApiError extends Error {
    public readonly response: ErrorResponse;

    constructor(response: ErrorResponse) {
        super(response.message || 'An API error occurred');
        this.name = 'ApiError';
        this.response = response;
    }
}

async function fetchWithConfig<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;

    const headers = {
        'Content-Type': 'application/json',
        ...options.headers,
    };

    const response = await fetch(url, {
        ...options,
        headers,
    });

    if (response.status === 204) {
        return undefined as T;
    }

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        if (data && data.status) {
            throw new ApiError(data as ErrorResponse);
        }
        throw new Error(response.statusText);
    }

    return data as T;
}

export const taskService = {
    listTasks: (page = 0, size = 10) =>
        fetchWithConfig<PageResult<TaskResource>>(`/tasks?page=${page}&size=${size}`, { method: 'GET' }),

    getTask: (id: string) =>
        fetchWithConfig<TaskResource>(`/tasks/${id}`, { method: 'GET' }),

    createTask: (data: CreateTaskRequest) =>
        fetchWithConfig<TaskResource>('/tasks', {
            method: 'POST',
            body: JSON.stringify(data)
        }),

    updateTask: (id: string, data: UpdateTaskRequest) =>
        fetchWithConfig<TaskResource>(`/tasks/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        }),

    deleteTask: (id: string) =>
        fetchWithConfig<void>(`/tasks/${id}`, { method: 'DELETE' }),
};
