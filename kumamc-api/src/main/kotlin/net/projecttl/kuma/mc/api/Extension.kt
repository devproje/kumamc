package net.projecttl.kuma.mc.api

import net.minestom.server.extensions.Extension
import net.projecttl.kuma.mc.api.model.Elevator

abstract class Extension : Extension() {

    private val evInfo = mutableListOf<Elevator>()

    fun addElevator(ev: Elevator) {
        evInfo += ev
    }

    abstract override fun initialize()
    abstract override fun terminate()
}