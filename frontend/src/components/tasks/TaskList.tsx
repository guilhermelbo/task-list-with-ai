'use client';

import { useState, useEffect } from 'react';

import TaskItem from './TaskItem';
import TaskFormDialog from './TaskFormDialog';
import { getTasks, createTask } from '@/services/api';
import { Task } from '@/types';

export default function TaskList() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        setLoading(true);
        const fetchedTasks = await getTasks();
        setTasks(fetchedTasks);
      } catch (err: any) {
        setError(err.message || 'Failed to load tasks');
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const handleCreateTask = async (taskData: Omit<Task, 'id'>) => {
    try {
      setError(null);
      const newTask = await createTask(taskData);
      setTasks([newTask, ...tasks]);
    } catch (err: any) {
      setError(err.message || 'Failed to create task');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 flex items-center justify-center">
        <div className="text-xl font-semibold text-gray-600">Loading tasks...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-4xl mx-auto">
        <header className="text-center mb-12">
          <h1 className="text-4xl sm:text-5xl font-bold bg-gradient-to-r from-gray-900 to-gray-700 bg-clip-text text-transparent mb-4">
            Task Manager
          </h1>
          <p className="text-xl text-gray-600 max-w-md mx-auto">Manage your tasks with priority and descriptions.</p>
        </header>
        <div className="space-y-6 mb-12">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 p-6 bg-white/70 backdrop-blur-sm rounded-2xl shadow-lg border border-white/50">
            <div>
              <h2 className="text-2xl font-bold text-gray-900">Your Tasks ({tasks.length})</h2>
            </div>
            <TaskFormDialog onSubmit={handleCreateTask} />
          </div>
        </div>
        {error && (
          <div className="mb-8 p-6 bg-red-50 border border-red-200 rounded-2xl">
            <p className="text-red-800 font-medium">{error}</p>
          </div>
        )}
        <div className="space-y-4">
          {tasks.length === 0 ? (
            <div className="text-center py-20 text-gray-500 bg-white rounded-2xl shadow-sm border border-gray-200">
              <svg className="mx-auto h-16 w-16 text-gray-400 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <h3 className="text-2xl font-semibold text-gray-900 mb-2">No tasks yet</h3>
              <p className="text-lg">Get started by creating your first task!</p>
            </div>
          ) : (
            tasks.map((task) => (
              <TaskItem key={task.id} task={task} />
            ))
          )}
        </div>
      </div>
    </div>
  );
}
