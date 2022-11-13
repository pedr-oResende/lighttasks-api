package com.resende.lighttasksserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LightTasksServerApplication

fun main(args: Array<String>) {
	runApplication<LightTasksServerApplication>(*args)
}
