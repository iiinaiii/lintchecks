package com.iiinaiii.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HttpCheckDetectorTest : LintDetectorTest() {
    companion object {
        private const val SOURCE_PATH = "src/com/iiinaiii/lintchecks/"
    }

    override fun getDetector(): Detector {
        return HttpCheckDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(HttpCheckDetector.ISSUE)
    }

    override fun allowCompilationErrors(): Boolean {
        return true
    }

    @Test
    @Throws(Exception::class)
    fun testDetectWarningJava() {
        val javaCode = """
                /**
                 * java class for test
                 * <p>see http://www.foo.jp/sample.</p>
                 */
                public class Foo {

                    private String fieldUrl = "http://www.foo.jp/field";

                    public Foo() {

                    }

                    // class comment http://sample.co.jp

                    /**
                     * javadoc
                     * <p>http://sample.co.jp</p>
                     */
                    public void startSampleRequest() {
                        String localUrl = "http://www.foo.jp/local";

                        // method comment http://sample.co.jp"
                        request(fieldUrl);

                        request("http://www.foo.jp/direct");
                    }

                    private void request(String url){

                    }
                }

            """
        val javaResult = lintProject(java(SOURCE_PATH + "Foo.java", javaCode))

        assertThat(javaResult).isEqualTo(
            """
src/com/iiinaiii/lintchecks/Foo.java:8: Error: Don't write http:// code direct!!! [WriteHttpDirect]
                    private String fieldUrl = "http://www.foo.jp/field";
                                              ~~~~~~~~~~~~~~~~~~~~~~~~~
src/com/iiinaiii/lintchecks/Foo.java:21: Error: Don't write http:// code direct!!! [WriteHttpDirect]
                        String localUrl = "http://www.foo.jp/local";
                                          ~~~~~~~~~~~~~~~~~~~~~~~~~
src/com/iiinaiii/lintchecks/Foo.java:26: Error: Don't write http:// code direct!!! [WriteHttpDirect]
                        request("http://www.foo.jp/direct");
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~
3 errors, 0 warnings

        """.trimIndent()
        )
    }

    @Test
    @Throws(Exception::class)
    fun testDetectWarningKotlin() {
        val kotlinCode = """
                /**
                 * Kotlin class for test
                 * <p>see http://www.foo.jp/sample.</p>
                 */
                class Foo{
                    private val fieldUrl = "http://www.foo.jp/field"

                    // class comment http://sample.co.jp

                    /**
                     * javadoc
                     *
                     * http://sample.co.jp
                     */
                    fun startSampleRequest() {
                        val localUrl = "http://www.foo.jp/local"

                        // method comment http://sample.co.jp"
                        request(fieldUrl)

                        request("http://www.foo.jp/direct")
                    }

                    private fun request(url: String) {}
                }
            """
        val kotlinResult = lintProject(LintDetectorTest.kotlin(SOURCE_PATH + "Foo.kt", kotlinCode))
        assertThat(kotlinResult).isEqualTo(
            """
src/com/iiinaiii/lintchecks/Foo.kt:7: Error: Don't write http:// code direct!!! [WriteHttpDirect]
                    private val fieldUrl = "http://www.foo.jp/field"
                                            ~~~~~~~~~~~~~~~~~~~~~~~
src/com/iiinaiii/lintchecks/Foo.kt:17: Error: Don't write http:// code direct!!! [WriteHttpDirect]
                        val localUrl = "http://www.foo.jp/local"
                                        ~~~~~~~~~~~~~~~~~~~~~~~
src/com/iiinaiii/lintchecks/Foo.kt:22: Error: Don't write http:// code direct!!! [WriteHttpDirect]
                        request("http://www.foo.jp/direct")
                                 ~~~~~~~~~~~~~~~~~~~~~~~~
3 errors, 0 warnings

        """.trimIndent()
        )
    }

    @Test
    @Throws(Exception::class)
    fun testDetectNoWarning() {
        val noWarningCode = """
                /**
                 * java class for test
                 * <p>see http://www.foo.jp/sample.</p>
                 */
                public class Foo {

                    public Foo() {
                    }

                    // class comment http://sample.co.jp

                    /**
                     * javadoc
                     * <p>http://sample.co.jp</p>
                     */
                    public void startSampleRequest() {
                        // method comment http://sample.co.jp"
                        request(null);
                    }

                    private void request(String url){
                    }
                }

            """
        val result = lintProject(java(SOURCE_PATH + "Foo.java", noWarningCode))
        assertThat(result).isEqualTo("No warnings.")
    }
}