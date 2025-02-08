package org.example.app

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

object DefaultScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports(
        "org.example.app.*"
    )
    jvm {
        dependenciesFromCurrentContext(
            "kotlin-scripting-jvm",
            "app"
        )
    }
}) {
    private fun readResolve(): Any = DefaultScriptConfiguration
}