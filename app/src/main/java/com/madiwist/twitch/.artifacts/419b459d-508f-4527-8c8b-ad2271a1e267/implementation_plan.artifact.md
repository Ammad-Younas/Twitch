# Bug Fix: Profile Navigation and Social Icons

This plan addresses two issues:
1. Navigation to Profile Screen from Posts.
2. Incorrect display of social icons when URLs are empty strings.

## User Review Required

> [!IMPORTANT]
> I will be adding `userId` to the `Post` domain model. This assumes that your backend API already provides this field in the JSON response for posts. If it doesn't, we might need to adjust how we identify the user.
> I will also change the social icon visibility check from `!= null` to `!isNullOrBlank()` to handle empty strings stored in the database.

## Proposed Changes

### Core Domain Models
#### [MODIFY] [Post.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/core/domain/models/Post.kt)
- Add `userId: String?` to the `Post` data class.

### Feature: Post
#### [MODIFY] [Post.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/domain/util/Post.kt)
- Update the `Post` composable to accept an `onUsernameClick` callback that passes the `userId`.
- Implement the click listener on the username text.

#### [MODIFY] [MainFeedScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedScreen.kt)
- Pass a navigation lambda to the `Post` component that navigates to the `ProfileScreen` with the correct `userId`.

### Feature: Profile
#### [MODIFY] [ProfileScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileScreen.kt)
- Update the `BannerSection` call to use `!isNullOrBlank()` for social link visibility checks.

#### [MODIFY] [BannerSection.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/components/BannerSection.kt)
- (Optional) Ensure the `BannerSection` itself is robust against empty strings if needed, though the check in `ProfileScreen` should suffice.

## Verification Plan

### Manual Verification
1. Open the app and go to the Main Feed.
2. Click on a username in a post. Verify it navigates to that user's profile.
3. Go to your own Profile. Verify that only icons for non-empty social links are displayed (as per the MongoDB state in your screenshot).
4. Go to Edit Profile, clear a social link, and save. Verify the icon disappears from the Profile screen.
