package org.example.app

import org.jetbrains.kotlin.scripting.ide_services.compiler.KJvmReplCompilerWithIdeServices
import org.jline.console.ScriptEngine
import org.jline.reader.Completer
import java.io.File
import java.nio.file.Path
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class KotlinScriptEngine : ScriptEngine {

    val host: BasicJvmScriptingHost

    init {
        // Create a new scripting host
        host = BasicJvmScriptingHost()
    }

    override fun getEngineName(): String? {
        return this::class.simpleName
    }

    override fun getExtensions(): Collection<String> {
        return listOf("kts")
    }

    override fun getScriptCompleter(): Completer? {
        TODO("Not yet implemented")
        // TODO: use kotlin scripting IDE services
        KJvmReplCompilerWithIdeServices()
    }

    override fun hasVariable(p0: String?): Boolean {
        // TOOD
        return false
    }

    override fun put(name: String?, value: Any?) {
        println("Putting $name = $value")
    }

    override fun get(p0: String?): Any? {
        TODO("Not yet implemented")
    }

    override fun find(p0: String?): Map<String?, Any?>? {
        TODO("Not yet implemented")
    }

    override fun del(vararg p0: String?) {
        // TODO
    }

    override fun toJson(p0: Any?): String? {
        TODO("Not yet implemented")
    }

    override fun toString(p0: Any?): String? {
        TODO("Not yet implemented")
    }

    override fun toMap(p0: Any?): Map<String?, Any?>? {
        TODO("Not yet implemented")
    }

    override fun deserialize(value: String?, format: String?): Any? {
        return if(format == "json") {
            mapOf<String, Any?>()
        } else {
            value
        }
    }

    override fun getSerializationFormats(): List<String> {
        return listOf("json", "none")
    }

    override fun getDeserializationFormats(): List<String?>? {
        return listOf("json", "kotlin", "none")
    }

    override fun persist(p0: Path?, p1: Any?) {

    }

    override fun persist(p0: Path?, p1: Any?, p2: String?) {
        TODO("Not yet implemented")
    }

    override fun execute(stmt: String): Any? {
        println("Executing: $stmt")

        val source = StringScriptSource(stmt)

        val resultWith = host.eval(source, DefaultScriptConfiguration, null)
        println(resultWith)

        return null
    }

    override fun execute(p0: File?, p1: Array<out Any?>?): Any? {
        TODO("Not yet implemented")
    }

    override fun execute(p0: Any?, vararg p1: Any?): Any? {
        TODO("Not yet implemented")
    }

}
