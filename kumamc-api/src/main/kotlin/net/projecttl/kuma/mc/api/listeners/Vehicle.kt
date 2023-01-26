package net.projecttl.kuma.mc.api.listeners

import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerMoveEvent
import net.projecttl.kuma.mc.api.utils.AreaUtils
import net.projecttl.kuma.mc.api.utils.VehicleData
class Vehicle(private val traffic: List<VehicleData>, val obj: String) {
    fun run(node: EventNode<Event>) {
        node.addListener(PlayerMoveEvent::class.java) { event ->
            traffic.forEach { station ->
                station.apply {
                    fun upper() {
                        AreaUtils(station.upper!!, true).apply {
                            if (event.player.inArea()) {
                                event.player.prev(traffic, obj)
                            }
                        }
                    }

                    fun lower() {
                        AreaUtils(station.lower!!, true).apply {
                            if (event.player.inArea()) {
                                event.player.next(traffic, obj)
                            }
                        }
                    }

                    try {
                        upper()
                    } catch (ex: NullPointerException) {
                        lower()
                    }

                    try {
                        lower()
                    } catch (ex: NullPointerException) {
                        upper()
                    }
                }
            }
        }
    }
}