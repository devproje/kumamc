package net.projecttl.kuma.mc.utils.perm

import net.minestom.server.entity.Player
import net.projecttl.kuma.mc.utils.perm.model.PermData
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

var Player.isPermed: Boolean
    get() = transaction {
        return@transaction !PermData.select { PermData.uuid eq uuid.toString() }.empty()
    }
    set(value) = transaction {
        if (!value) {
            PermData.deleteWhere { uuid eq this@isPermed.uuid.toString() }
            return@transaction
        }

        if (this@isPermed.isPermed) {
            return@transaction
        }

        PermData.insert {
            it[uuid] = this@isPermed.uuid.toString()
        }
    }