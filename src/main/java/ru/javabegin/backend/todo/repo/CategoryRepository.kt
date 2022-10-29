package ru.javabegin.backend.todo.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.javabegin.backend.todo.entity.Category

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByUserEmailOrderByTitleAsc(email: String): List<Category>

    @Query(
        "SELECT c FROM Category c WHERE " +
                "(:title IS NULL OR :title='' " +
                "OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                "AND c.user.email=:email " +
                "ORDER BY c.title ASC"
    )
    fun findByTitle(@Param("title") title: String?, @Param("email") email: String?): List<Category>
}