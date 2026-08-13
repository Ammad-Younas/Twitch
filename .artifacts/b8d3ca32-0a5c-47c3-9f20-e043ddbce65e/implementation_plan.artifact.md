# Fix Post Comment Count Display

This plan fixes the issue where the actual comment count is not displayed correctly on the `MainFeedScreen` and `ProfileScreen`. It involves updating the backend to reliably track comment counts and updating the frontend UI to display these counts in a more intuitive way.

## Proposed Changes

### Backend (Ktor)

The backend currently recalculates the comment count on every fetch. We will change this to use a persistent field in the `Post` document and increment it when a comment is created.

#### [MODIFY] [PostRepository.kt](file:///D:/MaDi/Practice/Ktor/Twitch_Backend/Twitch/src/main/kotlin/data/repository/post/PostRepository.kt)
- Add `updateCommentCount(postId: String, delta: Int)` to the interface.

#### [MODIFY] [PostRepositoryImpl.kt](file:///D:/MaDi/Practice/Ktor/Twitch_Backend/Twitch/src/main/kotlin/data/repository/post/PostRepositoryImpl.kt)
- Implement `updateCommentCount` using `Updates.inc`.
- Update `toPostResponse` to use the `commentCount` field from the `Post` document directly (more efficient).

#### [MODIFY] [CommentService.kt](file:///D:/MaDi/Practice/Ktor/Twitch_Backend/Twitch/src/main/kotlin/service/CommentService.kt)
- Call `postRepository.updateCommentCount` when a comment is successfully created or deleted.

---

### Frontend (Android)

We will update the UI components to display the comment count next to the comment icon and ensure the count is visible on all relevant screens.

#### [MODIFY] [PostItem.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/domain/util/PostItem.kt)
- Update `EngagementButtons` and `ActionRow` to accept and display `likeCount` and `commentCount` next to their respective icons.
- Remove the redundant count row at the bottom of the card.

#### [MODIFY] [PostDetailsScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/post_detail/PostDetailsScreen.kt)
- Update the header section to display the comment count next to the comment icon in the `ActionRow`.

---

## Verification Plan

### Automated Tests
- I will verify the code changes compile and follow the existing patterns. (Note: I cannot run the backend or Android emulator directly).

### Manual Verification
1. Add a comment to a post in the `PostDetailsScreen`.
2. Verify that the comment count increments immediately.
3. Refresh the `MainFeedScreen` or `ProfileScreen`.
4. Verify that the "actual" comment count is now displayed next to the comment icon on the post card.
