# Implement Instant Post Updates

This plan introduces a refresh mechanism to ensure that new posts appear immediately on the Feed and Profile screens after being published.

## User Review Required

> [!IMPORTANT]
> I will be adding a `SharedFlow` to the `PostRepository` to broadcast post creation events. ViewModels will listen to this flow to trigger data refreshes.
> This will ensure that when you navigate back from `CreatePostScreen`, the `MainFeedScreen` and `ProfileScreen` will already be refreshing or have refreshed.

## Proposed Changes

### Feature: Post
Implementing the refresh event mechanism.

#### [MODIFY] [PostRepository.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/domain/repository/PostRepository.kt)
- Add `val onPostCreated: SharedFlow<Unit>` to the interface.

#### [MODIFY] [PostRepositoryImpl.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/data/repository/PostRepositoryImpl.kt)
- Implement `onPostCreated` as a `MutableSharedFlow`.
- Emit a value to this flow after a successful `createPost` API call.

#### [MODIFY] [MainFeedViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedViewModel.kt)
- In `init`, collect the `onPostCreated` flow from the repository.
- When a value is received, trigger a refresh of the paging data (we might need a way to reach the `LazyPagingItems` or just expose a refresh trigger).
- *Alternative*: Just expose the flow to the UI and let `MainFeedScreen` call `posts.refresh()`.

#### [MODIFY] [CreatePostViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/create_post/CreatePostViewModel.kt)
- No changes needed here if `PostRepositoryImpl` handles the emission inside `createPost`.

### Feature: Profile
#### [MODIFY] [ProfileViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileViewModel.kt)
- Collect `onPostCreated` flow from `PostRepository`.
- Call `getProfile()` when a new post is created to update post counts (and eventually actual user posts).

## Verification Plan

### Manual Verification
1. Open the app to the Main Feed.
2. Click the FAB to Create Post.
3. Publish a post.
4. Verify that you are navigated back and the Feed automatically refreshes to show the new post at the top.
5. Go to your Profile and verify the post count has increased (if applicable) or the post list is updated.
