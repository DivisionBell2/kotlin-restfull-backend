package ru.javabegin.backend.todo.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.javabegin.backend.todo.entity.Category
import ru.javabegin.backend.todo.search.CategorySearchValues
import ru.javabegin.backend.todo.service.CategoryService

@RestController
@RequestMapping("/category")

class CategoryController(private val categoryService: CategoryService) {
    @PostMapping("/all")
    fun findAll(@RequestBody email: String): List<Category> {
        return categoryService.findAll(email)
    }

    @PostMapping("/add")
    fun add(@RequestBody category: Category): ResponseEntity<Any> {

        if (category.id != null && category.id != 0L) {
            return ResponseEntity<Any>("redundant param: id MUSt be null", HttpStatus.NOT_ACCEPTABLE)
        }

        if (category.title == null || category.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title MUST be not null", HttpStatus.NOT_ACCEPTABLE)
        }

        return ResponseEntity.ok(categoryService.add(category)) // возвращает добавленный объект
    }

    @PutMapping("/update")
    fun update(@RequestBody category: Category): ResponseEntity<*> {
        if (category.id == null || category.id == 0L) {
            return ResponseEntity<Any?>("missed param: id", HttpStatus.NOT_ACCEPTABLE)
        }

        if (category.title == null || category.title.trim { it <= ' ' }.length == 0) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }

        categoryService.update(category)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {

        try {
            categoryService.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id = $id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/search")
    fun search(@RequestBody categorySearchValues: CategorySearchValues): ResponseEntity<Any> {

        if (categorySearchValues.email == null || categorySearchValues.email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param email", HttpStatus.NOT_ACCEPTABLE)
        }

        val list = categoryService.findByTitle(categorySearchValues.title, categorySearchValues.email)
        return ResponseEntity.ok(list)
    }

    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        var category: Category = try {
            categoryService.findById(id)
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(category)
    }
}