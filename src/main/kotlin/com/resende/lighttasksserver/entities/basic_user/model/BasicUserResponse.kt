package com.resende.lighttasksserver.entities.basic_user.model

import com.resende.lighttasksserver.model.Status

class BasicUserResponse(
    val basicUser: BasicUser?,
    val status: Status
)