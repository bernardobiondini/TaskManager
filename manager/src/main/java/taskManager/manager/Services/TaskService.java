package taskManager.manager.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    if (task.getDueDays() != null) {
      LocalDate currentDate = LocalDate.now();
      LocalDate dueDate = currentDate.plusDays(task.getDueDays());
      task.setDueDate(dueDate);
    }

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
    getTaskInfo(task);
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
    getTaskInfo(task);
    return taskRepository.save(task);
  }

  public List<Task> findDoneTasks() {
    List<Task> tasks = taskRepository.findDoneTasks();
    if (tasks.isEmpty()) {
      return null;
    }
    tasks.forEach(this::getTaskInfo);
    return tasks;
  }

  public List<Task> findUndoneTasks() {
    List<Task> tasks = taskRepository.findUndoneTasks();
    if (tasks.isEmpty()) {
      return null;
    }
    tasks.forEach(this::getTaskInfo);
    return tasks;
  }

  public Task getById(Long id) {
    Task task = taskRepository.findById(id).orElse(null);
    getTaskInfo(task);

    return task;
  }

  private void getTaskInfo(Task task) {
    if (task != null) {
      String info = "Concluida";

      if (!task.isDone()) {
        info = "Prevista";
      }

      if (task.getDueDate() != null) {
        LocalDate currentDate = LocalDate.now();
        long delayed = 0;
        if (task.getDueDate().isBefore(currentDate)) {
          delayed = ChronoUnit.DAYS.between(task.getDueDate(), currentDate);
        }
        info += ", " + delayed + " dias de atraso";
      }

      task.setInfo(info);
    }
  }
}
