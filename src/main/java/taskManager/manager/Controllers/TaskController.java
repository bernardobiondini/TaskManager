package taskManager.manager.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import taskManager.manager.DTO.TaskDTO;
import taskManager.manager.Models.Task;
import taskManager.manager.Services.TaskService;

@RestController
@RequestMapping(value = "/task")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @PostMapping()
  @Operation(summary = "Cria uma tarefa")
  public ResponseEntity<Task> save(@RequestBody @Valid TaskDTO task) {
    try {
      Task result = taskService.save(task.toTask());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "{id}/done")
  @Operation(summary = "Marca a tarefa como Concluida")
  public ResponseEntity<Task> setAsDone(@PathVariable Long id) {
    try {
      Task result = taskService.setAsDone(id);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "{id}/undone")
  @Operation(summary = "Marca a tarefa como Não Concluida")
  public ResponseEntity<Task> setAsUndone(@PathVariable Long id) {
    try {
      Task result = taskService.setAsUndone(id);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/done")
  @Operation(summary = "Lista tarefas concluidas")
  public ResponseEntity<List<Task>> getDoneTasks() {
    try {
      List<Task> result = taskService.findDoneTasks();

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/undone")
  @Operation(summary = "Lista tarefas não concluidas")
  public ResponseEntity<List<Task>> getUnDoneTasks() {
    try {
      List<Task> result = taskService.findUndoneTasks();

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}