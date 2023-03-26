package net.projecttl.kuma.mc.api.model

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec

data class Spawn(val loc: Pos, val area: Area, var height: Double? = null)
