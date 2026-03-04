export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface TaskResource {
  id: string;
  title: string;
  completed: boolean;
  description: string;
  priority: Priority;
  createdAt: string;
  updatedAt: string;
  _ui_meta?: {
    canEditTitle: boolean;
    canDelete: boolean;
  };
  links?: { rel: string; href: string }[];
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  priority: Priority;
}

export interface UpdateTaskRequest {
  title?: string;
  description?: string;
  priority?: Priority;
  completed?: boolean;
}

export interface PageResult<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}

export interface ChatRequest {
  message: string;
  sessionId?: string;
}

export interface ChatResponse {
  response: string;
  sessionId: string;
}
