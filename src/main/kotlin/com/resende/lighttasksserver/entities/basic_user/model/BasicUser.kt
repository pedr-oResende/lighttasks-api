package com.resende.lighttasksserver.entities.basic_user.model

import com.resende.lighttasksserver.entities.teams.model.Team
import com.resende.lighttasksserver.entities.tasks.model.Task
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "basic_users")
class BasicUser(
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    val id: Long,

    @NotBlank
    val username: String?,

    @NotBlank
    val full_name: String?,

    @OneToMany(mappedBy = "responsible")
    val tasks: Set<Task>?,

    @ManyToMany
    val teams: Set<Team>?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BasicUser

        return Objects.equals(username, other.username)
                && Objects.equals(full_name, other.full_name)
                && Objects.equals(tasks, other.tasks)
                && Objects.equals(teams, other.teams)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    fun copy(
        id: Long = this.id,
        username: String? = this.username,
        full_name: String? = this.full_name,
        tasks: Set<Task>? = this.tasks,
        teams: Set<Team>? = this.teams
    ) = BasicUser(
        id = id,
        username = username,
        full_name = full_name,
        tasks = tasks,
        teams = teams
    )
}