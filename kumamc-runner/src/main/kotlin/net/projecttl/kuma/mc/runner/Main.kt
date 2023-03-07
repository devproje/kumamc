package net.projecttl.kuma.mc.runner

import net.projecttl.kuma.mc.KumaCore

suspend fun main() {
    val core = KumaCore()
    core.load()
    core.run()
}