package com.resende.lighttasksserver.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtils {
    companion object {
        fun getDate(): String? {
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return dtf.format(LocalDateTime.now())
        }

        fun getDateTime(): String? {
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return dtf.format(LocalDateTime.now())
        }
    }
}