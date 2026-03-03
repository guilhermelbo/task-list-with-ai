'use client';

import { useState } from 'react';
import { Priority } from '@/types';
import { cn } from '@/lib/utils';

interface Props {
  onSubmit: (data: { title: string; description: string; priority: Priority }) => Promise<void>;
}

export function TaskFormDialog({ onSubmit }: Props) {
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState({ title: '', description: '', priority: 'LOW' as Priority });
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await onSubmit(formData);
      setFormData({ title: '', description: '', priority: 'LOW' });
      setOpen(false);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <button
        onClick={() => setOpen(true)}
        className="inline-flex items-center justify-center px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white font-medium text-sm rounded-xl hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 shadow-lg hover:shadow-xl transition-all duration-200 whitespace-nowrap"
        disabled={submitting}
      >
        + New Task
      </button>
      <dialog
        open={open}
        onClose={() => !submitting && setOpen(false)}
        className={cn(
          'fixed inset-0 z-50 flex items-center justify-center p-4',
          open && 'backdrop:bg-black/40 backdrop-blur-sm'
        )}
      >
        <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full max-h-[90vh] overflow-hidden flex flex-col">
          <div className="p-8 pb-6 border-b border-gray-200">
            <h2 className="text-2xl font-bold text-gray-900">Create New Task</h2>
          </div>
          <form onSubmit={handleSubmit} className="p-8 space-y-6 flex-1 overflow-y-auto">
            <div className="space-y-2">
              <label htmlFor="title" className="block text-sm font-semibold text-gray-700">
                Title *
              </label>
              <input
                id="title"
                type="text"
                required
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all text-lg placeholder:text-gray-400"
                placeholder="Enter task title"
                disabled={submitting}
              />
            </div>
            <div className="space-y-2">
              <label htmlFor="description" className="block text-sm font-semibold text-gray-700">
                Description
              </label>
              <textarea
                id="description"
                rows={4}
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl resize-vertical focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all placeholder:text-gray-400"
                placeholder="Enter task description (optional)"
                disabled={submitting}
              />
            </div>
            <div className="space-y-2">
              <label htmlFor="priority" className="block text-sm font-semibold text-gray-700">
                Priority
              </label>
              <select
                id="priority"
                value={formData.priority}
                onChange={(e) => setFormData({ ...formData, priority: e.target.value as Priority })}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all bg-white text-lg font-medium appearance-none bg-no-repeat bg-right pr-10"
                disabled={submitting}
              >
                <option value="LOW">🟢 Low</option>
                <option value="MEDIUM">🟡 Medium</option>
                <option value="HIGH">🔴 High</option>
              </select>
            </div>
            <div className="flex gap-3 pt-4">
              <button
                type="button"
                onClick={() => setOpen(false)}
                className="flex-1 px-6 py-3 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-xl transition-all font-medium disabled:opacity-50"
                disabled={submitting}
              >
                Cancel
              </button>
              <button
                type="submit"
                className="flex-1 px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white hover:from-blue-700 hover:to-blue-800 rounded-xl font-semibold transition-all shadow-lg hover:shadow-xl focus:outline-none focus:ring-4 focus:ring-blue-300 disabled:opacity-50"
                disabled={submitting}
              >
                {submitting ? 'Creating...' : 'Create Task'}
              </button>
            </div>
          </form>
        </div>
      </dialog>
    </>
  );
}