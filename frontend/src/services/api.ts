'use client';

import { Task } from '@/types';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

export async function getTasks(): Promise<Task[]> {
    const res = await fetch(`${API_URL}/tasks`);
    if (!res.ok) throw new Error('Failed to fetch tasks');
    const data = await res.json();
    // Handle HATEOAS response
    return data._embedded?.tasks || data || [];
}

export async function createTask(taskData: Omit<Task, 'id'>): Promise<Task> {
    const res = await fetch(`${API_URL}/tasks`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(taskData),
    });
    if (!res.ok) throw new Error('Failed to create task');
    return res.json();
}