import { TaskResource } from "@/types";
import { Checkbox } from "@/components/ui/checkbox";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Trash2, Edit2 } from "lucide-react";
import TaskFormDialog from "./TaskFormDialog";

interface TaskItemProps {
    task: TaskResource;
    onDelete: () => void;
    onToggleComplete: () => void;
    onTaskUpdated: () => void;
}

export default function TaskItem({ task, onDelete, onToggleComplete, onTaskUpdated }: TaskItemProps) {
    // Dumb UI: Rely purely on HATEOAS links and UI meta from the backend
    const canDelete = !!task._links?.delete;
    const canEditTitle = task._ui_meta?.canEditTitle ?? true;
    const canUpdateState = !!task._links?.update;

    return (
        <div className={`p-4 rounded-lg border ${task.completed ? 'bg-zinc-50/50 dark:bg-zinc-900/20' : 'bg-white dark:bg-zinc-950'} transition-colors group hover:shadow-sm`}>
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 sm:gap-4">
                <div className="flex items-start sm:items-center space-x-4 sm:space-x-3 flex-1 min-w-0 overflow-hidden">
                    <Checkbox
                        checked={task.completed}
                        onCheckedChange={onToggleComplete}
                        disabled={!canUpdateState}
                        className="h-5 w-5 flex-shrink-0 mt-0.5 sm:mt-0"
                    />
                    <div className="flex flex-col min-w-0 flex-1 space-y-1">
                        <div className="flex items-center space-x-2">
                            <span className={`text-base truncate transition-all font-medium ${task.completed ? 'line-through text-zinc-400 dark:text-zinc-600' : 'text-zinc-900 dark:text-zinc-100'}`}>
                                {task.title}
                            </span>
                            {task.priority && (
                                <Badge
                                    variant={task.priority === "HIGH" ? "destructive" : task.priority === "MEDIUM" ? "secondary" : "outline"}
                                    className="text-xs px-2 py-0.5 flex-shrink-0"
                                >
                                    {task.priority.toLowerCase()}
                                </Badge>
                            )}
                        </div>
                        {task.description && (
                            <p className={`text-sm leading-relaxed transition-all ${task.completed ? 'line-through text-zinc-500 dark:text-zinc-500' : 'text-zinc-600 dark:text-zinc-400'}`}>
                                {task.description}
                            </p>
                        )}
                    </div>
                </div>

            <div className="flex items-center space-x-1 opacity-0 group-hover:opacity-100 transition-opacity">
                {canEditTitle && (
                    <TaskFormDialog
                        existingTask={task}
                        onTaskSaved={onTaskUpdated}
                        trigger={
                            <Button variant="ghost" size="icon" className="h-8 w-8 text-zinc-500 hover:text-blue-500">
                                <Edit2 className="h-4 w-4" />
                                <span className="sr-only">Edit</span>
                            </Button>
                        }
                    />
                )}

                {canDelete && (
                    <Button
                        variant="ghost"
                        size="icon"
                        onClick={onDelete}
                        className="h-8 w-8 text-zinc-500 hover:text-red-500 flex-shrink-0"
                    >
                        <Trash2 className="h-4 w-4" />
                        <span className="sr-only">Delete</span>
                    </Button>
                )}
            </div>
        </div>

        <div className="flex items-center space-x-1 opacity-0 group-hover:opacity-100 transition-opacity ml-auto sm:ml-0">
            {canEditTitle && (
                <TaskFormDialog
                    existingTask={task}
                    onTaskSaved={onTaskUpdated}
                    trigger={
                        <Button variant="ghost" size="icon" className="h-8 w-8 text-zinc-500 hover:text-blue-500">
                            <Edit2 className="h-4 w-4" />
                            <span className="sr-only">Edit</span>
                        </Button>
                    }
                />
            )}
            {canDelete && (
                <Button
                    variant="ghost"
                    size="icon"
                    onClick={onDelete}
                    className="h-8 w-8 text-zinc-500 hover:text-red-500 flex-shrink-0"
                >
                    <Trash2 className="h-4 w-4" />
                    <span className="sr-only">Delete</span>
                </Button>
            )}
        </div>
    </div>
        </div>
    );
}
