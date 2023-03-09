package net.projecttl.kuma.mc.api

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec

data class Spawn(val loc: Pos, val area: Pair<Vec, Vec>)
