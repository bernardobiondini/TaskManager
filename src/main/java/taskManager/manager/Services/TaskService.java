package taskManager.manager.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    Task task = taskRepository.getReferenceById(id);
    if (task.isDone()) {
      return task;
    }
    task.setDone(true);
    return taskRepository.save(task);
  }

  public Task setAsUndone(Long id) {
    Task task = taskRepository.getReferenceById(id);
    if (!task.isDone()) {
      return task;
    }
    task.setDone(false);
    return taskRepository.save(task);
  }

  public List<Task> findDoneTasks() {
    return taskRepository.findDoneTasks();
  }

  public List<Task> findUndoneTasks() {
    return taskRepository.findUndoneTasks();
  }

}
