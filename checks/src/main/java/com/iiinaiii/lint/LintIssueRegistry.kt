package com.iiinaiii.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class LintIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(HttpCheckDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API

    override val vendor = Vendor(
        feedbackUrl = "kyoumi155@gmail.com",
        identifier = "com.iiinaiii.lint",
        vendorName = "Naoki ISHII",
    )
}