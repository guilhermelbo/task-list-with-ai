import { TaskResource } from "@/types";
import { Checkbox } from "@/components/ui/checkbox";
import { Button } from "@/components/ui/button";
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
        <div className={`flex items-center justify-between p-4 rounded-lg border ${task.completed ? 'bg-zinc-50/50 dark:bg-zinc-900/20' : 'bg-white dark:bg-zinc-950'} transition-colors group`}>
            <div className="flex items-center space-x-4 overflow-hidden">
                <Checkbox
                    checked={task.completed}
                    onCheckedChange={onToggleComplete}
                    disabled={!canUpdateState}
                    className="h-5 w-5"
                />
                <span className={`text-base truncate transition-all ${task.completed ? 'line-through text-zinc-400 dark:text-zinc-600' : 'text-zinc-900 dark:text-zinc-100'}`}>
                    {task.title}
                </span>
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
                        className="h-8 w-8 text-zinc-500 hover:text-red-500"
                    >
                        <Trash2 className="h-4 w-4" />
                        <span className="sr-only">Delete</span>
                    </Button>
                )}
            </div>
        </div>
    );
}
