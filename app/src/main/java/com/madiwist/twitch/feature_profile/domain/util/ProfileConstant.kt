package com.madiwist.twitch.feature_profile.domain.util

object ProfileConstant {
    const val MAXIMUM_SKILLS = 3

    val GITHUB_PROFILE_REGEX = """^(https?://)?(www\.)?github\.com/[A-Za-z0-9-]+/?$""".toRegex()
    val INSTAGRAM_PROFILE_REGEX = """^(https?://)?(www\.)?instagram\.com/[A-Za-z0-9_.]+/?$""".toRegex()
    val LINKEDIN_PROFILE_REGEX = """^(https?://)?(www\.)?linkedin\.com/in/[A-Za-z0-9-]+/?$""".toRegex()
}