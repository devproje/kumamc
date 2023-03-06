package net.projecttl.kuma.mc

import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

class PropLoader {
    private val prop = Properties()
    private val path = Path.of("./server.properties")

    val world = prop.getProperty("")

    init {
        try {
            if (!Files.exists(path))
        }
    }
}