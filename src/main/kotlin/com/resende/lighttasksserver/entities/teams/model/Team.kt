package com.resende.lighttasksserver.entities.teams.model

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.tasks.model.Task
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "teams")
class Team(
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    val id: Long,

    @NotBlank
    val name: String?,

    @ManyToMany
    @JoinTable(
        name = "teams_basic_user",
        joinColumns = [JoinColumn(name = "teams_fk")],
        inverseJoinColumns = [JoinColumn(name = "basic_users_fk")]
    )
    val members: Set<BasicUser>?,

    @OneToMany
    val tasks: Set<Task>?,

    @NotBlank
    val created_at: String?,

    @NotBlank
    val leader_id: Long?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Team

        return name == other.name
                && members == other.members
                && created_at == other.created_at
                && leader_id == other.leader_id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    fun copy(
        id: Long = this.id,
        name: String? = this.name,
        members: Set<BasicUser>? = this.members,
        createdAt: String? = this.created_at,
        leaderId: Long? = this.leader_id,
        tasks: Set<Task>? = this.tasks
    ) = Team(
        id = id,
        name = name,
        members = members,
        created_at = createdAt,
        leader_id = leaderId,
        tasks = tasks
    )
}
