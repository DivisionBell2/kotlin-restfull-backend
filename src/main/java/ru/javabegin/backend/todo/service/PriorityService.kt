package ru.javabegin.backend.todo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.javabegin.backend.todo.entity.Priority
import ru.javabegin.backend.todo.repo.PriorityRepository

/*
Всегда нужно создавать отдельный класс Service для доступа к данным, даже если кажется,
что мало методов или это все можно реализовать сразу в контроллере
Такой подход полезен для будущих доработок и правильной архитектуры (особенно, если работаете с транзакциями
 */
@Service
/*
Все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
если в методе возникает исключение - все выполненные операции из данного метода откатятся (Rollback)
 */
@Transactional
// сервис имеет право обращаться к репозиторию (БД)
class PriorityService(private val repository: PriorityRepository) {
    fun findAll(email: String): List<Priority> {
        return repository.findByUserEmailOrderByIdAsc(email)
    }

    fun add(priority: Priority): Priority {
        return repository.save(priority) // метод save обновляет или создает новый объект, если его не было
    }

    fun update(priority: Priority): Priority {
        return repository.save(priority) // метод save обновляет или создает новый объект, если его не было
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    fun findById(id: Long): Priority {
        return repository.findById(id).get() // т.к. возвращается Optional - можно получить объект методом get()
    }

    fun find(title: String?, email: String): List<Priority> {
        return repository.findByTitle(title, email)
    }
}