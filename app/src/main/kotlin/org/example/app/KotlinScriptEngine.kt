package org.example.app

import org.jetbrains.kotlin.scripting.ide_services.compiler.KJvmReplCompilerWithIdeServices
import org.jline.console.ScriptEngine
import org.jline.reader.Completer
import java.io.File
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class KotlinScriptEngine : ScriptEngine {

    val variables = mutableMapOf<String, Any?>()

    val evaluatedSnippets = mutableListOf<Any>()

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

    override fun put(name: String, value: Any?) {
        println("Putting $name = $value")
        variables[name] = value
    }

    override fun get(name: String): Any? {
        println("Getting $name")
        return variables[name]
    }

    override fun find(p0: String?): Map<String?, Any?>? {
        TODO("Not yet implemented")
    }

    override fun del(vararg name: String?) {
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

        // Not sure why jline does this
        if(stmt == "_executionResult ? 0 : 1") {
            val result = get("_executionResult")
            return if(result != null) {
                0
            } else {
                1
            }
        }


        val source = StringScriptSource(stmt)

        val scriptConfig = ScriptCompilationConfiguration({
            defaultImports(
                "org.example.app.*"
            )
            providedProperties(*variables.filter { it.value != null }.map { it.key to KotlinType(it.value!!::class) }.toTypedArray())
            jvm {
                dependenciesFromCurrentContext(
                    "kotlin-scripting-jvm",
                    "app"
                )
            }
        })

        val evalConfig = if (evaluatedSnippets.isEmpty()) {
            ScriptEvaluationConfiguration {
                /*jvm {
                    scriptsInstancesSharing(true)
                }*/
            }
        } else {
            ScriptEvaluationConfiguration {
                /*jvm {
                    scriptsInstancesSharing(true)
                }*/
                //previousSnippets(*evaluatedSnippets.toTypedArray())
                providedProperties(*variables.filter { it.value != null }.map { it.key to it.value }.toTypedArray())
            }
        }

        val resultWith = host.eval(source, scriptConfig, evalConfig)
        println(resultWith)

        if(resultWith is ResultWithDiagnostics.Success) {
            val resultValue = resultWith.value.returnValue
            val scriptInstance = resultValue.scriptInstance

            if( scriptInstance != null) {
                evaluatedSnippets += scriptInstance

                scriptInstance::class.declaredMembers.forEach { callable ->
                    variables[callable.name] = callable.call(scriptInstance)
                }
            }

            return when(resultValue) {
                is ResultValue.Unit -> {
                    null
                }

                is ResultValue.Value -> {
                    resultValue.value
                }
                is ResultValue.Error -> TODO()
                ResultValue.NotEvaluated -> TODO()
            }
        }

        return null
    }

    override fun execute(p0: File?, p1: Array<out Any?>?): Any? {
        TODO("Not yet implemented")
    }

    override fun execute(p0: Any?, vararg p1: Any?): Any? {
        TODO("Not yet implemented")
    }

}
