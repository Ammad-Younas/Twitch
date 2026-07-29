# Bug Fix: Instant Updates and Missing Posts

This plan addresses the issue where new posts don't appear immediately on the feed and why only one post might be showing.

## User Review Required

> [!IMPORTANT]
> I've identified that the `SharedFlow` used for refresh events is dropping events because the screen is not active when the post is created. I will switch to a more robust event propagation method.
> Regarding the "Only one post showing" issue: Since you restarted the app and still only see one post, it suggests the backend might only be returning one. Please verify if the user you are logged in as follows themselves or if there's any filtering on the backend.

## Proposed Changes

### Feature: Post
Ensuring refresh events are captured and paging data is reloaded correctly.

#### [MODIFY] [PostRepositoryImpl.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/data/repository/PostRepositoryImpl.kt)
- Update `_onPostCreated` to use a `Channel` or a `SharedFlow` with `replay = 1`. A `SharedFlow` with `replay = 1` is better for multiple observers.
- This ensures that when the `MainFeedViewModel` starts (or resumes collecting), it will see the "last created" event.

#### [MODIFY] [MainFeedViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedViewModel.kt)
- Change `_eventFlow` to use a `Channel` or increase `replay`.
- Alternatively, make the `posts` flow reactive to the refresh trigger using `flatMapLatest`. This is the most reliable way to refresh Paging data.

#### [MODIFY] [MainFeedScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedScreen.kt)
- Ensure the `collect` logic is robust.

## Verification Plan

### Manual Verification
1. Create a new post.
2. Verify that upon returning to the feed, the new post is visible at the top (Instant Update).
3. Check the total count of posts. If it still shows only one after a fresh restart, we will add logging to `PostSource` to see exactly what the API returns.
