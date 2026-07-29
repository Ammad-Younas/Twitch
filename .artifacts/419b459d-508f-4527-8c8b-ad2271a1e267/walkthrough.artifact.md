# Bug Fix: Instant Post Updates

I have fixed the issue where new posts were not appearing instantly on the Feed without a manual app restart.

## Changes Made

### Feature: Post
Improved the reliability of the refresh event propagation.

- **[MainFeedViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedViewModel.kt)**:
    - Updated `_eventFlow` to use `replay = 1`.
    - **Reasoning**: Previously, the refresh event was emitted while the Feed screen was in the background. Because the flow had no "memory" (replay), the UI missed the signal when it resumed. By adding `replay = 1`, the UI will now receive the last refresh signal as soon as it starts collecting again upon returning from the "Create Post" screen.

## Verification Results

### Manual Verification
> [!TIP]
> 1. Create a new post.
> 2. Once the post is published and you are navigated back to the Feed, the list should now automatically refresh and display the new post at the top.
> 3. No app restart should be required.
