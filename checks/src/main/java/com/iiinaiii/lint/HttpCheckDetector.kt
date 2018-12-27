package com.iiinaiii.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.getValueIfStringLiteral
import java.util.*

class HttpCheckDetector : Detector(), Detector.UastScanner {

    companion object {
        val ISSUE = Issue.create(
            "WriteHttpDirect",
            "Write http url direct in code",
            "Don't write http/https url direct in code.",
            Category.CORRECTNESS,
            7,
            Severity.ERROR,
            Implementation(
                HttpCheckDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return Collections.singletonList(ULiteralExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitLiteralExpression(node: ULiteralExpression) {
                val string = node.getValueIfStringLiteral() ?: return
                if (string.contains("http://") || string.contains("https://")) {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "Don't write http:// code direct!!!"
                    )
                }
            }
        }
    }
}