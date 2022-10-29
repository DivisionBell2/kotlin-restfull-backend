package ru.javabegin.backend.todo.aop

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class LoggingAspect {
    companion object{
        val log: Logger = LogManager.getLogger(LoggingAspect::class.java.name)
    }

    @Around("execution(* ru.javabegin.backend.todo.controller..*(..)))")
    @Throws(Throwable::class)
    fun profileControllerMethods(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val methodSignature = proceedingJoinPoint.signature as MethodSignature

        val className = methodSignature.declaringType.simpleName
        val methodName = methodSignature.name
        LoggingAspect.log.info("-------- Executing $className.$methodName   ----------- ")
        val countDown = StopWatch()

        countDown.start()

        val result = proceedingJoinPoint.proceed()
        countDown.stop()
        LoggingAspect.log.info("----------- Execution time of " + className + "." + methodName + " :: " + countDown.totalTimeMillis + " ms")
        return result
    }
}