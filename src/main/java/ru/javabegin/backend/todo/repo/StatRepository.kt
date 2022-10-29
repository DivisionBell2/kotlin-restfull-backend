package ru.javabegin.backend.todo.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.javabegin.backend.todo.entity.Stat

@Repository
interface StatRepository : CrudRepository<Stat, Long> {
    fun findByUserEmail(email: String): Stat
}