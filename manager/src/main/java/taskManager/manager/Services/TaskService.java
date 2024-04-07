package taskManager.manager.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import taskManager.manager.Models.Task;
import taskManager.manager.Repositories.TaskRepository;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  public Task save(Task task) {
    return taskRepository.save(task);
  }

  public Task setAsDone(Long id) {
    Task task = this.getById(id);
    if (task == null) {
      return null;
    }
    if (task.isDone()) {
      return task;
    }
    task.setDone(true);
    return taskRepository.save(task);
  }

  public Task setAsUndone(Long id) {
    Task task = this.getById(id);
    if (task == null) {
      return null;
    }
    if (!task.isDone()) {
      return task;
    }
    task.setDone(false);
    return taskRepository.save(task);
  }

  public List<Task> findDoneTasks() {
    List<Task> tasks = taskRepository.findDoneTasks();
    if (tasks.isEmpty()) {
      return null;
    }
    return tasks;
  }

  public List<Task> findUndoneTasks() {
    List<Task> tasks = taskRepository.findUndoneTasks();
    if (tasks.isEmpty()) {
      return null;
    }
    return tasks;
  }

  private Task getById(Long id) {
    try {
      return taskRepository.getReferenceById(id);
    } catch (EntityNotFoundException e) {
      System.out.println("Task not found for id: " + id);
      return null;
    }
  }
}
