package ru.javabegin.backend.todo.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.javabegin.backend.todo.entity.Priority

@Repository
interface PriorityRepository : JpaRepository<Priority, Long> {
    fun findByUserEmailOrderByIdAsc(email: String?): List<Priority>

    @Query(
        "SELECT p FROM Priority p where " +
                "(:title is null or :title='' " +
                " or lower(p.title) like lower(concat('%', :title,'%'))) " +
                " and p.user.email=:email " +
                "order by p.title asc"
    )
    fun findByTitle(@Param("title") title: String?, @Param("email") email: String?): List<Priority>
}