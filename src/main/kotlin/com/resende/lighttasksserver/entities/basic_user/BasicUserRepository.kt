package com.resende.lighttasksserver.entities.basic_user

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.model.Status
import org.springframework.data.jpa.repository.JpaRepository

interface BasicUserRepository : JpaRepository<BasicUser, Long>