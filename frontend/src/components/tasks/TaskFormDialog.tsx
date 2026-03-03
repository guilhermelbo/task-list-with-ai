"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Priority, TaskResource } from "@/types";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { taskService } from "@/services/api";
import { TaskResource } from "@/types";
import { Plus } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

interface TaskFormDialogProps {
  existingTask?: TaskResource;
  onTaskSaved: () => void;
  trigger?: React.ReactNode;
}

export default function TaskFormDialog({ existingTask, onTaskSaved, trigger }: TaskFormDialogProps) {
  const [open, setOpen] = useState(false);
  const [title, setTitle] = useState(existingTask?.title || "");
  const [description, setDescription] = useState(existingTask?.description || "");
  const [priority, setPriority] = useState<Priority>(existingTask?.priority || "MEDIUM");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});

  const isEditing = !!existingTask;

  // Dumb UI: The form only checks basic empty string, everything else relies on backend
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setValidationErrors({});

    try {
      if (isEditing) {
        await taskService.updateTask(existingTask.id, {
          title,
          description,
          priority,
          completed: existingTask.completed
        });
      } else {
        await taskService.createTask({ title, description, priority });
      }
      setOpen(false);
      setTitle("");
      onTaskSaved();
    } catch (err: any) {
      if (err.response?.validationErrors) {
        setValidationErrors(err.response.validationErrors);
      } else {
        setError(err.message || "An unexpected error occurred.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleOpenChange = (newOpen: boolean) => {
    setOpen(newOpen);
    if (newOpen) {
      setTitle(existingTask?.title || "");
      setDescription(existingTask?.description || "");
      setPriority(existingTask?.priority || "MEDIUM");
      setError(null);
      setValidationErrors({});
    }
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogTrigger asChild>
        {trigger || (
          <Button size="sm" className="gap-1">
            <Plus className="h-4 w-4" />
            Add Task
          </Button>
        )}
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>{isEditing ? "Edit Task" : "Create Task"}</DialogTitle>
            <DialogDescription>
              {isEditing ? "Make changes to your task here." : "Add a new task to your list."}
            </DialogDescription>
          </DialogHeader>

          <div className="grid gap-4 py-6">
            <div className="flex flex-col gap-2">
              <Label htmlFor="title">Title</Label>
              <Input
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Buy groceries..."
                className={validationErrors.title ? 'border-red-500 focus-visible:ring-red-500' : ''}
              />
              {validationErrors.title && (
                <span className="text-sm text-red-500">{validationErrors.title}</span>
              )}
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="description">Description (optional)</Label>
              <Textarea
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Enter task description..."
                className={validationErrors.description ? 'border-red-500 focus-visible:ring-red-500' : ''}
              />
              {validationErrors.description && (
                <span className="text-sm text-red-500">{validationErrors.description}</span>
              )}
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="priority">Priority</Label>
              <Select value={priority} onValueChange={(value: Priority) => setPriority(value)}>
                <SelectTrigger id="priority">
                  <SelectValue placeholder="Select priority" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="LOW">Low</SelectItem>
                  <SelectItem value="MEDIUM">Medium</SelectItem>
                  <SelectItem value="HIGH">High</SelectItem>
                </SelectContent>
              </Select>
              {validationErrors.priority && (
                <span className="text-sm text-red-500">{validationErrors.priority}</span>
              )}
            </div>

            {error && (
              <div className="text-sm text-red-500 p-3 bg-red-50 dark:bg-red-950/50 rounded-md">
                {error}
              </div>
            )}
          </div>

          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => setOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" disabled={loading || !title.trim()}>
              {loading ? "Saving..." : (isEditing ? "Update Task" : "Create Task")}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
