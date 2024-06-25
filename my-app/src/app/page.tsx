"use client"
import styles from "./page.module.css";
import { useState, useEffect } from "react";
import axios from "axios";
import { Task, TaskPriority, TaskType } from './task'; // Import the Task interface and enums

export default function Home() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [newTask, setNewTask] = useState('');

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
    if (newTask.trim()) {
      try {
        const newTaskObj = { title: newTask, description: 'lorem ipsum', type: TaskType.FREE, priority: TaskPriority.MEDIUM }; // Adjust the default values as necessary
        const response = await axios.post<Task>('https://taskmanager-x0b7.onrender.com/api/task', newTaskObj);
        setTasks([...tasks, response.data]);
        setNewTask('');
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
        <div className={styles.todo}>
          <h2>A FAZER</h2>
          {tasks.filter(task => !task.done).map(task => (
            <div key={task.id} className={styles.task}>
              <div className={styles.taskContent}>
                <h3>{task.title}</h3>
                <p>{task.description}</p>
              </div>
              <input 
                type="checkbox" 
                onChange={() => toggleTaskCompletion(task.id, true)} 
              />
            </div>
          ))}
          <input 
            type="text" 
            value={newTask} 
            onChange={(e) => setNewTask(e.target.value)} 
            placeholder="Nova tarefa"
            className={styles.taskInput}
          />
          <button onClick={addTask} className={styles.addButton}>Adicionar</button>
        </div>
        <div className={styles.done}>
          <h2>FEITO</h2>
          {tasks.filter(task => task.done).map(task => (
            <div key={task.id} className={`${styles.task} ${styles.completed}`}>
              <div className={styles.taskContent}>
                <h3>{task.title}</h3>
                <p>{task.description}</p>
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
