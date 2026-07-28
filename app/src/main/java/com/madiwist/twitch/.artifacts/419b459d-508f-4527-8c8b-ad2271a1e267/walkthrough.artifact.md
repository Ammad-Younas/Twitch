# Bug Fix: Social Icons Visibility

I have fixed the issue where social icons (Github, Instagram, LinkedIn) were showing even if their URLs were empty.

## Changes Made

- **[ProfileScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileScreen.kt)**:
    - Updated the visibility logic for social icons in the `BannerSection`.
    - Changed the check from `!= null` to `!isNullOrBlank()`. This correctly handles cases where the database contains an empty string (`""`) for the social link, ensuring the icon is hidden in such cases.

## Verification Results

> [!TIP]
> You can verify this by checking the Profile screen for a user who has empty strings for some social links in MongoDB. The corresponding icons should now be hidden.
