package ru.javabegin.backend.todo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.javabegin.backend.todo.entity.Stat
import ru.javabegin.backend.todo.repo.StatRepository

/*
всегда нужно создавать отдельный класс Service для доступа к данным, даже если кажется,
что мало методов или это все можно реализовать сразу в контроллере
Такой подход полезен для будущих доработок и правильной архитектуры (особенно, если работаете с транзакциями)
 */
@Service
/*
 все методы класса должны выполняться без ошибки, чтобы транзакция завершилась
 если в методе возникнет исключение - все выполненные операции откатятся (Rollback)
 */
@Transactional
// сервис имеет право обращаться к репозиторию (БД)
class StatService(private val repository: StatRepository) {
    fun findStat(email: String): Stat {
        return repository.findByUserEmail(email)
    }
}