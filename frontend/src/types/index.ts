export interface TaskResource {
  id: string;
  title: string;
  completed: boolean;
  _ui_meta?: {
    canEditTitle: boolean;
    canDelete: boolean;
  };
  _links?: Record<string, { href: string }>;
}

export interface CreateTaskRequest {
  title: string;
}

export interface UpdateTaskRequest {
  title: string;
  completed: boolean;
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
