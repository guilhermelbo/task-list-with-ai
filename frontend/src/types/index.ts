export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface TaskResource {
  id: string;
  title: string;
  completed: boolean;
  description: string;
  priority: Priority;
  _ui_meta?: {
    canEditTitle: boolean;
    canDelete: boolean;
  };
  _links?: Record<string, { href: string }>;
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
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}
