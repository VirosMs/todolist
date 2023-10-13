package com.charles.todolist.task;

import com.charles.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setUserId((UUID) idUser);

        var currentDate = LocalDateTime.now();


        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data de inicio/fim nao pode ser menor que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data de inicio nao pode ser menor que a data de fim");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
    /**
     * <a href="http://localhost:8080/tasks/89234723-cdgasdasd-54894151">...</a>
     *
     * @param request request
     * @return ResponseEntity
     */
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        return this.taskRepository.findByUserId((UUID) idUser);
    }
    // http://localhost:8080/tasks/89234723-cdgasdasd-54894151
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var task = this.taskRepository.findById(id).orElse(null);

        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found this task");
        }

        var idUser = request.getAttribute("idUser");

        if(!task.getUserId().equals(idUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Unauthorized to update this task");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }


}
