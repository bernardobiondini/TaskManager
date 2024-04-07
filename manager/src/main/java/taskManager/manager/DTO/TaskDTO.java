package taskManager.manager.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import taskManager.manager.Models.Task;

@Data
public class TaskDTO {
  @NotBlank
  private String title;

  @NotBlank
  private String description;

  private Boolean done = false;

  public Task toTask() {
    Task task = new Task();
    task.setTitle(title);
    task.setDescription(description);
    task.setDone(done);
    return task;
  }
}
