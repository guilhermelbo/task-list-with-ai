'use client';

import { TaskResource, CreateTaskRequest, UpdateTaskRequest, PageResult } from '@/types';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

export const taskService = {
    listTasks: async (page: number, size: number = 10): Promise<PageResult<TaskResource>> => {
        const res = await fetch(`${API_URL}/tasks?page=${page}&size=${size}`);
        if (!res.ok) throw new Error('Failed to fetch tasks');
        return res.json();
    },

    createTask: async (taskData: CreateTaskRequest): Promise<TaskResource> => {
        const res = await fetch(`${API_URL}/tasks`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(taskData),
        });
        if (!res.ok) throw new Error('Failed to create task');
        return res.json();
    },

    updateTask: async (id: string, taskData: UpdateTaskRequest): Promise<TaskResource> => {
        const res = await fetch(`${API_URL}/tasks/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(taskData),
        });
        if (!res.ok) throw new Error('Failed to update task');
        return res.json();
    },

    deleteTask: async (id: string): Promise<void> => {
        const res = await fetch(`${API_URL}/tasks/${id}`, {
            method: 'DELETE',
        });
        if (!res.ok) throw new Error('Failed to delete task');
    }
};