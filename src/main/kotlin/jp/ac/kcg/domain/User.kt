package jp.ac.kcg.domain

import org.hibernate.validator.constraints.Email
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
class User {

    @Id
    @Size(min = 3, max = 32)
    @Column(name = "user_id", nullable = false)
    var userName: String? = null

    @Size(min = 4, max = 64)
    @Column(name = "screen_name", nullable = false)
    var screenName: String? = null

    @Column(name = "password", nullable = false)
    var password: String? = null

    @Column(name = "mail_address", nullable = true)
    @Size(min = 10, max = 128)
    @Email
    var mailAddress: String? = null

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean? = null
}
