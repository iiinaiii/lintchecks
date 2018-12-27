package com.iiinaiii.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class LintIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(HttpCheckDetector.ISSUE)

}