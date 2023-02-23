package net.projecttl.kuma.mc.api.util.perm.model

import org.jetbrains.exposed.sql.Table

object PermData : Table("perm") {
    val uuid = varchar("uuid", 36)
    override val primaryKey = PrimaryKey(uuid, name = "uuid")
}