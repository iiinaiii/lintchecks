package com.iiinaiii.lint.lin

import com.android.tools.lint.detector.api.Issue
import com.serchinastico.lin.test.LintTest
import com.serchinastico.lin.test.LintTest.Expectation.NoErrors
import com.serchinastico.lin.test.LintTest.Expectation.SomeError
import org.junit.Test

class LinHttpCheckDetectorKtTest : LintTest {
    override val issue: Issue
        get() = CheckHttpDirectDetector.issue

    @Test
    fun inJavaClass_whenContainsHttp_detectsErrors() {
        expect(
            """
                package foo;

                import android.app.Activity;
                import android.os.Bundle;
                import android.view.View;

                public class TestClass extends Activity {
                   private String url = "http://sample.com";
                }
            """.inJava
        ) toHave SomeError("src/foo/TestClass.java")
    }

    @Test
    fun inJavaClass_whenNotContainsHttp_detectsNoErrors() {
        expect(
            """
                |package foo;
                |
                |import android.app.Activity;
                |import android.os.Bundle;
                |import android.view.View;
                |
                |public class TestClass extends Activity {
                |  private String value = "some value";
                |}
            """.inJava
        ) toHave NoErrors
    }

    @Test
    fun inKotlinClass_whenContainsHttp_detectsErrors() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass {
                |   private val url: String = "http://sample.com"
                |}
            """.inKotlin
        ) toHave SomeError("src/foo/TestClass.kt")
    }

    @Test
    fun inKotlinClass_whenNotContainsHttp_detectsNoErrors() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass {
                |   private val value: String = "some value"
                |}
            """.inKotlin
        ) toHave NoErrors
    }

}