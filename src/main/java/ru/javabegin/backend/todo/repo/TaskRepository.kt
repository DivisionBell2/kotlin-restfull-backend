package ru.javabegin.backend.todo.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.javabegin.backend.todo.entity.Task
import java.util.*

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    @Query(
        "SELECT t FROM Task t WHERE " +
                "(:title IS NULL OR :title='' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND" +
                "(:completed IS NULL OR t.completed=:completed) AND " +
                "(:priorityId IS NULL or t.priority.id=:priorityId) AND " +
                "(:categoryId IS NULL OR t.category.id=:categoryId) AND " +
                "(" +
                "(CAST(:dateFrom AS timestamp) IS NULL OR t.taskDate>=:dateFrom) AND " +
                "(CAST(:dateTo AS timestamp) IS NULL OR t.taskDate<=:dateTo)" +
                ") AND " +
                "(t.user.email=:email)"
    )
    fun findByParams(
        @Param("title") title: String?,
        @Param("completed") completed: Boolean?,
        @Param("priorityId") priorityId: Long?,
        @Param("categoryId") categoryId: Long?,
        @Param("email") email: String,
        @Param("dateFrom") dateFrom: Date?,
        @Param("dateTo") dateTo: Date?,
        pageable: Pageable
    ): Page<Task>

    fun findByUserEmailOrderByTitleAsc(email: String): List<Task>
}