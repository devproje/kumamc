package net.projecttl.kuma.mc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.velocity.VelocityProxy
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.utils.NamespaceID
import net.projecttl.kuma.mc.handler.CampfireHandler
import net.projecttl.kuma.mc.handler.SignHandler
import net.projecttl.kuma.mc.handler.SkullHandler
import net.projecttl.kuma.mc.listeners.Listener
import net.projecttl.kuma.mc.tasks.TabList
import net.projecttl.kuma.mc.utils.perm.model.PermData
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

var logger: ComponentLogger = MinecraftServer.LOGGER
lateinit var instance: InstanceContainer
lateinit var owner: UUID

class Core {
    private val server = MinecraftServer.init()
    private var configPath = Path.of("./server.properties")
    private val handler = MinecraftServer.getGlobalEventHandler()

    fun props(): Properties {
        val props = Properties()
        try {
            if (Files.exists(configPath)) {
                props.load(Files.newInputStream(configPath))
                return props
            }

            val stream = javaClass.classLoader.getResourceAsStream("server.properties")
            props.apply {
                load(stream)
                store(Files.newOutputStream(configPath), "Minestom ${MinecraftServer.VERSION_NAME}")
            }
        } catch (ex: Exception) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }

        return props
    }

    fun mcInit() {
        MinecraftServer.getBlockManager().apply {
            registerHandler(NamespaceID.from(Key.key("minecraft:sign"))) { SignHandler() }
            registerHandler(NamespaceID.from(Key.key("minecraft:skull"))) { SkullHandler() }
            registerHandler(NamespaceID.from(Key.key("minecraft:campfire"))) { CampfireHandler() }
        }

        Listener.run(handler, this)
        when (props().getProperty("default-tab-list")) {
            "true" -> TabList.run()
            "false" -> return
            else -> {
                logger.warn(Component.text("you must include boolean type for `default-tab-list` option"))
                return
            }
        }
    }

    fun dbInit() {
        val dataDir = Path.of("./data")
        if (!Files.exists(dataDir)) {
            Files.createDirectory(dataDir)
        }

        val data = File("data", "data.db")
        if (!data.exists()) {
            data.createNewFile()
        }

        Database.connect("jdbc:sqlite:./${data.path}", driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(PermData)
        }
    }

    fun serverInit() {
        try {
            when (props().getProperty("online-mode")) {
                "true" -> MojangAuth.init()
                "false" -> logger.warn("You're not enabled online-mode")
                else -> throw IllegalStateException()
            }

        } catch (e: Exception) {
            MinecraftServer.getExceptionManager().handleException(e)
        }

        val secret = props().getProperty("velocity-secret")
        when (props().getProperty("proxy-type")) {
            "none" -> {}

            "velocity" -> {
                if (secret == "") {
                    throw NullPointerException()
                }

                VelocityProxy.enable(secret)
                logger.info("Enabled velocity forward")
            }

            "bungeecord" -> {
                BungeeCordProxy.enable()
                logger.info("Enabled bungeecord forward")
            }

            else -> throw IllegalStateException()
        }

        if (props().getProperty("owner-uuid") != "") {
            owner = UUID.fromString(props().getProperty("owner-uuid"))
        }
    }

    suspend fun run() {
        coroutineScope {
            launch {
                val addr = props().getProperty("server-ip")
                val port = props().getProperty("server-port").toInt()

                logger.info("Minecraft ${MinecraftServer.VERSION_NAME}")
                logger.info("server binding port at: $port")
                server.start(addr, port)
            }
        }
    }
}