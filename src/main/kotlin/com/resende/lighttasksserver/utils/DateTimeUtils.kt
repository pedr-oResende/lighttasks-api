package com.resende.lighttasksserver.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtils {
    companion object {
        fun getDate(): String? {
            val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return dtf.format(LocalDateTime.now())
        }

        fun getDateTime(): String? {
            val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            return dtf.format(LocalDateTime.now())
        }
    }
}