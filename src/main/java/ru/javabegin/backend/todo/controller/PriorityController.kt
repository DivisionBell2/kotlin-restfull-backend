package ru.javabegin.backend.todo.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.javabegin.backend.todo.entity.Priority
import ru.javabegin.backend.todo.search.PrioritySearchValues
import ru.javabegin.backend.todo.service.PriorityService

@RestController
@RequestMapping("/priority")

class PriorityController(private val prioritySerivce: PriorityService) {
    @PostMapping("/all")
    fun findAll(@RequestBody email: String): List<Priority> {
        return prioritySerivce.findAll(email)
    }

    @PostMapping("/add")
    fun add(@RequestBody priority: Priority): ResponseEntity<Any> {
        if (priority.id != null && priority.id != 0L) {
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }

        if (priority.title == null || priority.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }

        if (priority.color == null || priority.color.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: color", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(prioritySerivce.add(priority))
    }

    @PutMapping("/update")
    fun update(@RequestBody priority: Priority): ResponseEntity<Any> {
        if (priority.id == null || priority.id == 0L) {
            return ResponseEntity<Any>("missed param: id", HttpStatus.NOT_ACCEPTABLE)
        }

        if (priority.title == null || priority.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed parar: title", HttpStatus.NOT_ACCEPTABLE)
        }

        if (priority.color == null || priority.color.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: color", HttpStatus.NOT_ACCEPTABLE)
        }

        prioritySerivce.update(priority)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        var priority: Priority? = null

        priority = try {
            prioritySerivce.findById(id)
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(priority)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        try {
            prioritySerivce.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/search")
    fun search(@RequestBody prioritySearchValues: PrioritySearchValues): ResponseEntity<Any> {

        if (prioritySearchValues.email == null || prioritySearchValues.email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: email", HttpStatus.NOT_ACCEPTABLE)
        }

        return ResponseEntity.ok(prioritySerivce.find(prioritySearchValues.title, prioritySearchValues.email)
        )
    }
}