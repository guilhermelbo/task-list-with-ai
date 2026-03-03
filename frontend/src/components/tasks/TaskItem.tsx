'use client';

import { PriorityBadge } from '../ui/badge';
import { Task } from '@/types';
import { cn } from '@/lib/utils';

interface Props {
  task: Task;
  className?: string;
}

export function TaskItem({ task, className }: Props) {
  return (
    <article className={cn(
      'flex flex-col sm:flex-row sm:items-center justify-between p-6 border border-gray-200 rounded-xl shadow-sm hover:shadow-md hover:border-gray-300 transition-all duration-200',
      className
    )}>
      <div className="flex-1 min-w-0 mb-4 sm:mb-0 sm:mr-4">
        <h3 className="text-xl font-semibold text-gray-900 mb-2 truncate">{task.title}</h3>
        {task.description && (
          <p className="text-base text-gray-600 leading-relaxed line-clamp-3">{task.description}</p>
        )}
      </div>
      <PriorityBadge priority={task.priority} />
    </article>
  );
}