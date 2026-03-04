'use client';

import { ChatRequest, ChatResponse } from '@/types';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

export const aiService = {
    chat: async (request: ChatRequest): Promise<ChatResponse> => {
        const res = await fetch(`${API_URL}/ai/chat`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request),
        });
        if (!res.ok) throw new Error('Failed to communicate with AI');
        return res.json();
    }
};
