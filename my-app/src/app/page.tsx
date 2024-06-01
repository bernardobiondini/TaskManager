"use client"
import styles from "./page.module.css";
import { useState } from "react";

export default function Home() {
  const [tasks, setTasks] = useState([
    { id: 1, title: 'Tarefa 1', description: 'lorem ipsum', completed: false }
  ]);
  const [newTask, setNewTask] = useState('');

  const addTask = () => {
    if (newTask.trim()) {
      const newTaskObj = { id: tasks.length + 1, title: newTask, description: 'lorem ipsum', completed: false };
      setTasks([...tasks, newTaskObj]);
      setNewTask('');
    }
  };

  const toggleTaskCompletion = (taskId: number) => {
    setTasks(tasks.map(task => 
      task.id === taskId ? { ...task, completed: !task.completed } : task
    ));
  };

  return (
    <div className={styles.container}>
      <div className={styles.todoContainer}>
        <div className={styles.todo}>
          <h2>A FAZER</h2>
          {tasks.filter(task => !task.completed).map(task => (
            <div key={task.id} className={styles.task}>
              <div className={styles.taskContent}>
                <h3>{task.title}</h3>
                <p>{task.description}</p>
              </div>
              <input 
                type="checkbox" 
                onChange={() => toggleTaskCompletion(task.id)} 
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
          {tasks.filter(task => task.completed).map(task => (
            <div key={task.id} className={`${styles.task} ${styles.completed}`}>
              <div className={styles.taskContent}>
                <h3>{task.title}</h3>
                <p>{task.description}</p>
              </div>
              <input 
                type="checkbox" 
                checked 
                onChange={() => toggleTaskCompletion(task.id)} 
              />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
