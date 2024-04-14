package taskManager.manager.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import taskManager.manager.Enums.TaskPriority;
import taskManager.manager.Enums.TaskType;
import taskManager.manager.Models.Task;

@Data
public class TaskDTO {
  @NotBlank
  private String title;

  @NotBlank
  private String description;

  private Boolean done = false;

  @FutureOrPresent
  private LocalDate dueDate;

  private Integer dueDays;

  private TaskPriority priority;

  public Task toTask() {
    TaskType type = TaskType.LIVRE;

    if (dueDate != null) {
      type = TaskType.DATA;
    }

    if (dueDays != null) {
      type = TaskType.PRAZO;
    }

    Task task = new Task();
    task.setTitle(title);
    task.setDescription(description);
    task.setDone(done);
    task.setDueDate(dueDate);
    task.setDueDays(dueDays);
    task.setType(type);
    task.setPriority(priority != null ? priority : TaskPriority.ALTA);
    return task;
  }
}
