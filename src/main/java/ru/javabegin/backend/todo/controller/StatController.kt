package ru.javabegin.backend.todo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.javabegin.backend.todo.entity.Stat
import ru.javabegin.backend.todo.service.StatService

@RestController
class StatController(private val statService: StatService) {
    @PostMapping("/stat")
    fun findByEmail(@RequestBody email: String): ResponseEntity<Stat> {
        return ResponseEntity.ok(statService.findStat(email))
    }
}