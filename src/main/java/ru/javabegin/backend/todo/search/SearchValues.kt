package ru.javabegin.backend.todo.search

import java.util.*

data class CategorySearchValues(val email: String, val title: String?)

data class PrioritySearchValues(val email: String, val title: String?)

data class TaskSearchValues(
    val email: String,
    val pageNumber: Int,
    val pageSize: Int,
    val sortColumn: String,
    val sortDirection: String
) {
    val title: String? = null
    val completed: Int? = null
    val priorityId: Long? = null
    val categoryId: Long? = null
    val dateFrom: Date? = null
    val dateTo: Date? = null
}