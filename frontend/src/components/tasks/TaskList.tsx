"use client";

import { Skeleton } from "@/components/ui/skeleton";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import { useCallback } from "react";

import { useState, useEffect } from "react";
import { taskService } from "@/services/api";
import { PageResult, TaskResource } from "@/types";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import TaskItem from "./TaskItem";
import TaskFormDialog from "./TaskFormDialog";

export default function TaskList() {
    const [tasks, setTasks] = useState<TaskResource[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchTasks = async () => {
        try {
            setLoading(true);
            const data = await taskService.listTasks(page, pageSize);
            setPageData(data);
            setCurrentPage(page);
            setError(null);
        } catch (err: any) {
            setError(err.message || "Failed to load tasks");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    const handleDelete = async (id: string, deleteLink?: string) => {
        try {
            await taskService.deleteTask(id);
            if (pageData?.content.length === 1 &amp;&amp; currentPage &gt; 0) {
                setCurrentPage(currentPage - 1);
                fetchTasks(currentPage - 1);
            } else {
                refresh();
            }
        } catch (err: any) {
            alert(err.message || "Failed to delete task");
        }
    };

    const handleToggleComplete = async (task: TaskResource) => {
        if (!task._links?.update) return;
        try {
            await taskService.updateTask(task.id, {
                completed: !task.completed,
            });
            refresh();
        } catch (err: any) {
            alert(err.message || "Failed to update task");
        }
    };

    return (
        <Card className="w-full max-w-2xl mx-auto shadow-sm border-zinc-200 dark:border-zinc-800">
            <CardHeader className="flex flex-row items-center justify-between pb-4">
                <div>
                    <CardTitle className="text-2xl font-bold">Tasks</CardTitle>
                    <CardDescription>Manage your daily to-dos.</CardDescription>
                </div>
                <TaskFormDialog onTaskSaved={fetchTasks} />
            </CardHeader>
            <CardContent>
                {error && <div className="text-red-500 mb-4">{error}</div>}

                {loading ? (
                    <div className="text-center py-8 text-zinc-500">Loading tasks...</div>
                ) : tasks.length === 0 ? (
                    <div className="text-center py-12 text-zinc-500 border-2 border-dashed rounded-lg">
                        No tasks found. Add a new one to get started!
                    </div>
                ) : (
                    <div className="space-y-3">
                        {tasks.map((task) => (
                            <TaskItem
                                key={task.id}
                                task={task}
                                onDelete={() => handleDelete(task.id, task._links?.delete?.href)}
                                onToggleComplete={() => handleToggleComplete(task)}
                                onTaskUpdated={fetchTasks}
                            />
                        ))}
                    </div>
                )}
            </CardContent>
        </Card>
    );
}
