package net.projecttl.kuma.mc.api.model

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec

data class MapData(val spawn: Pos, val area: Pair<Vec, Vec>, val height: Int)
