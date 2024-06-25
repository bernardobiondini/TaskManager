"use client"
import styles from "./page.module.css";
import { useState, useEffect } from "react";
import axios from "axios";
import { Task, TaskPriority, TaskDTO } from './task'; // Import the Task interface and enums

export default function Home() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [newTaskTitle, setNewTaskTitle] = useState<string>('');
  const [newTaskDescription, setNewTaskDescription] = useState<string>('');
  const [dueDate, setDueDate] = useState<string | null>(null);
  const [dueDays, setDueDays] = useState<number | null>(null);
  const [priority, setPriority] = useState<TaskPriority>(TaskPriority.MEDIUM);
  const [taskType, setTaskType] = useState<string>('LIVRE'); // New state for task type

  useEffect(() => {
    // Fetch initial tasks from API
    const fetchTasks = async () => {
      try {
        const doneTasks = await axios.get<Task[]>('https://taskmanager-x0b7.onrender.com/api/task/done');
        const undoneTasks = await axios.get<Task[]>('https://taskmanager-x0b7.onrender.com/api/task/undone');
        setTasks([...undoneTasks.data, ...doneTasks.data]);
      } catch (error) {
        console.error("Error fetching tasks:", error);
      }
    };

    fetchTasks();
  }, []);

  const addTask = async () => {
    if (newTaskTitle.trim() && newTaskDescription.trim()) {
      try {
        let newTaskObj: TaskDTO = {
          title: newTaskTitle,
          description: newTaskDescription,
          done: false,
          priority: priority
        };

        if (taskType === 'DATA') {
          newTaskObj.dueDate = dueDate ? new Date(dueDate).toISOString().split('T')[0] : null;
          newTaskObj.dueDays = null;
        } else if (taskType === 'PRAZO') {
          newTaskObj.dueDate = null;
          newTaskObj.dueDays = dueDays;
        } else {
          newTaskObj.dueDate = null;
          newTaskObj.dueDays = null;
        }

        const response = await axios.post<Task>('https://taskmanager-x0b7.onrender.com/api/task', newTaskObj);
        setTasks([...tasks, response.data]);
        setNewTaskTitle('');
        setNewTaskDescription('');
        setDueDate(null);
        setDueDays(null);
        setPriority(TaskPriority.MEDIUM);
        setTaskType('LIVRE');
      } catch (error) {
        console.error("Error adding task:", error);
      }
    }
  };

  const toggleTaskCompletion = async (taskId: number, completed: boolean) => {
    try {
      const url = `https://taskmanager-x0b7.onrender.com/api/task/${taskId}/${completed ? 'done' : 'undone'}`;
      const response = await axios.post<Task>(url);
      setTasks(tasks.map(task => task.id === taskId ? response.data : task));
    } catch (error) {
      console.error("Error toggling task completion:", error);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.todoContainer}>
        <div className={styles.formContainer}>
          <h2>Adicionar Nova Tarefa</h2>
          <input 
            type="text" 
            value={newTaskTitle} 
            onChange={(e) => setNewTaskTitle(e.target.value)} 
            placeholder="Título da nova tarefa"
            className={styles.taskInput}
          />
          <textarea
            value={newTaskDescription}
            onChange={(e) => setNewTaskDescription(e.target.value)}
            placeholder="Descrição da nova tarefa"
            className={styles.taskInput}
          />
          <select 
            value={taskType}
            onChange={(e) => setTaskType(e.target.value)}
            className={styles.taskInput}
          >
            <option value="LIVRE">Livre</option>
            <option value="DATA">Data Específica</option>
            <option value="PRAZO">Prazo</option>
          </select>
          {taskType === 'DATA' && (
            <input 
              type="date" 
              value={dueDate || ''} 
              onChange={(e) => setDueDate(e.target.value)} 
              placeholder="Data de vencimento"
              className={styles.taskInput}
            />
          )}
          {taskType === 'PRAZO' && (
            <input 
              type="number" 
              value={dueDays || ''} 
              onChange={(e) => setDueDays(Number(e.target.value))} 
              placeholder="Dias para vencer"
              className={styles.taskInput}
            />
          )}
          <select 
            value={priority} 
            onChange={(e) => setPriority(e.target.value as TaskPriority)} 
            className={styles.taskInput}
          >
            <option value={TaskPriority.LOW}>Baixa</option>
            <option value={TaskPriority.MEDIUM}>Média</option>
            <option value={TaskPriority.HIGH}>Alta</option>
          </select>
          <button onClick={addTask} className={styles.addButton}>Adicionar</button>
        </div>
        <div className={styles.todo}>
          <h2>A FAZER</h2>
          {tasks.filter(task => !task.done).map(task => (
            <div key={task.id} className={styles.task}>
              <div className={styles.taskContent}>
                <h3>{task.title}</h3>
                <p>{task.description}</p>
                <p>{task.info}</p> {/* Display the task info */}
              </div>
              <input 
                type="checkbox" 
                onChange={() => toggleTaskCompletion(task.id, true)} 
              />
            </div>
          ))}
        </div>
        <div className={styles.done}>
          <h2>FEITO</h2>
          {tasks.filter(task => task.done).map(task => (
            <div key={task.id} className={`${styles.task} ${styles.completed}`}>
              <div className={styles.taskContent}>
                <h3>{task.title}</h3>
                <p>{task.description}</p>
                <p>{task.info}</p> {/* Display the task info */}
              </div>
              <input 
                type="checkbox" 
                checked 
                onChange={() => toggleTaskCompletion(task.id, false)} 
              />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
