package com.resende.lighttasksserver.entities.basic_user

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import org.springframework.data.jpa.repository.JpaRepository

interface BasicUserRepository : JpaRepository<BasicUser, Long>