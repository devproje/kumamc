package net.projecttl.kuma.mc.api.model

import net.minestom.server.coordinate.Vec

data class Elevator(val loc: Vec, val floors: MutableList<Double>)
