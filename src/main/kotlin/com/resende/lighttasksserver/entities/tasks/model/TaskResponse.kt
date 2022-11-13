package com.resende.lighttasksserver.entities.tasks.model

import com.resende.lighttasksserver.model.Status

class TaskResponse(
    val task: TaskDTO? = null,
    val status: Status
)