package org.example.app

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.defaultJvmScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(fileExtension = "kts", compilationConfiguration = SnippetScriptConfiguration::class)
abstract class SnippetScript

object SnippetScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports(
        "org.example.app.*"
    )
    jvm {
        dependenciesFromCurrentContext(
            "kotlin-scripting-jvm"
        )
    }
}) {
    private fun readResolve(): Any = SnippetScriptConfiguration
}