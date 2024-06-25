export enum TaskPriority {
  LOW = 'BAIXA',
  MEDIUM = 'MEDIA',
  HIGH = 'ALTA'
}

export enum TaskType {
  DATE = 'DATA',
  DUE = 'PRAZO',
  FREE = "LIVRE"
}

export interface Task {
  id: number;
  title: string;
  description: string;
  done: boolean;
  type: TaskType;
  priority: TaskPriority;
  dueDate: string | null; // Assuming the date is in ISO format
  dueDays: number | null;
  info?: string;
}
