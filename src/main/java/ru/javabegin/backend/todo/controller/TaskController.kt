package ru.javabegin.backend.todo.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.javabegin.backend.todo.entity.Task
import ru.javabegin.backend.todo.search.TaskSearchValues
import ru.javabegin.backend.todo.service.TaskService
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/task")

class TaskController(private val taskService: TaskService) {
    @PostMapping("/all")
    fun findAll(@RequestBody email: String?): ResponseEntity<List<Task>> {
        return ResponseEntity.ok(
            taskService.findAll(
                email!!
            )
        )
    }

    @PostMapping("/add")
    fun add(@RequestBody task: Task): ResponseEntity<Any> {

        if (task.id != null && task.id != 0L) {
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }

        if (task.title == null || task.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }

        return ResponseEntity.ok(taskService.add(task))
    }

    @PutMapping("/update")
    fun update(@RequestBody task: Task): ResponseEntity<Any> {

        if (task.id == null || task.id == 0L) {
            return ResponseEntity<Any>("missed param id", HttpStatus.NOT_ACCEPTABLE)
        }

        if (task.title == null || task.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }

        taskService.update(task)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {

        try {
            taskService.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        val task: Task = try {
            taskService.findById(id)
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(task)
    }

    @PostMapping("/search")
    fun search(@RequestBody taskSearchValues: TaskSearchValues): ResponseEntity<Any> {

        val title = taskSearchValues.title

        val completed = taskSearchValues.completed != null && taskSearchValues.completed == 1
        val priorityId = taskSearchValues.priorityId
        val categoryId = taskSearchValues.categoryId
        val sortColumn = if (taskSearchValues.sortColumn != null) taskSearchValues.sortColumn else null
        val sortDirection = if (taskSearchValues.sortDirection != null) taskSearchValues.sortDirection else null
        val pageNumber = taskSearchValues.pageNumber
        val pageSize = taskSearchValues.pageSize
        val email = if (taskSearchValues.email != null) taskSearchValues.email else null

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: email", HttpStatus.NOT_ACCEPTABLE)
        }

        var dateFrom: Date? = null
        var dateTo: Date? = null

        if (taskSearchValues.dateFrom != null) {
            val calendarFrom = Calendar.getInstance()
            calendarFrom.time = taskSearchValues.dateFrom
            calendarFrom[Calendar.HOUR_OF_DAY] = 0
            calendarFrom[Calendar.MINUTE] = 0
            calendarFrom[Calendar.SECOND] = 0
            calendarFrom[Calendar.MILLISECOND] = 1
            dateFrom = calendarFrom.time
        }

        if (taskSearchValues.dateTo != null) {
            val calendarTo = Calendar.getInstance()
            calendarTo.time = taskSearchValues.dateTo
            calendarTo[Calendar.HOUR_OF_DAY] = 23
            calendarTo[Calendar.MINUTE] = 59
            calendarTo[Calendar.SECOND] = 59
            calendarTo[Calendar.MILLISECOND] = 999
            dateTo = calendarTo.time
        }

        val direction = if (sortDirection == null || sortDirection.trim().isEmpty() || sortDirection.trim { it <= ' ' } == "asc") Direction.ASC else Direction.DESC

        val sort = Sort.by(direction, sortColumn, ID_COLUMN)

        val pageRequest = PageRequest.of(pageNumber, pageSize, sort)

        val result =
            taskService.findByParams(title, completed, priorityId, categoryId, email, dateFrom, dateTo, pageRequest)

        return ResponseEntity.ok(result)
    }

    companion object {
        const val ID_COLUMN = "id"
    }
}