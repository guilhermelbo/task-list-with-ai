import * as React from 'react'

import { cn } from '@/lib/utils'

import { Priority } from '@/types'

interface PriorityBadgeProps {
  priority: Priority
  className?: string
  children?: React.ReactNode
}

export function PriorityBadge({ priority, className, children = priority, ...props }: PriorityBadgeProps) {
  const priorityClass = {
    LOW: 'bg-green-100 hover:bg-green-200 text-green-800 border border-green-200',
    MEDIUM: 'bg-yellow-100 hover:bg-yellow-200 text-yellow-800 border border-yellow-200',
    HIGH: 'bg-red-100 hover:bg-red-200 text-red-800 border border-red-200',
  }[priority]

  return (
    <span
      className={cn(
        'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ring-offset-background transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2',
        priorityClass,
        className
      )}
      {...props}
    >
      {children}
    </span>
  )
}