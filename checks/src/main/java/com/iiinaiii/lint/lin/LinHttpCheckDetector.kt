package com.iiinaiii.lint.lin

import com.android.tools.lint.detector.api.Scope
import com.serchinastico.lin.annotations.Detector
import com.serchinastico.lin.dsl.*
import org.jetbrains.uast.getValueIfStringLiteral

@Detector
fun checkHttpDirect() = detector(
    issue(
        scope = Scope.JAVA_FILE_SCOPE,
        description = "<Lin> Write http url direct in code",
        explanation = "<Lin> Don't write http/https url direct in code."
    )
) {
    literalExpression {
        suchThat { node ->
            val stringLiteral = node.getValueIfStringLiteral() ?: return@suchThat false
            return@suchThat stringLiteral.contains("http://") || stringLiteral.contains("https://")
        }
    }
}