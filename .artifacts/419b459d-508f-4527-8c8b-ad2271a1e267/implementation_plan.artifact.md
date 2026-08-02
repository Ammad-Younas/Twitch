# Bug Fix: Profile Posts Not Showing

This plan addresses the issue where the posts list on the Profile screen is empty even though the user has posts.

## User Review Required

> [!IMPORTANT]
> I've identified that the `ProfileViewModel` was missing the "Refresh" signal when a new post was created, and the posts list in the UI lacked error/loading state handling.
> I will also refactor the `posts` flow to be reactive. If the initial `userId` is empty (own profile), the flow will automatically restart with the correct `userId` once the profile information is loaded from the backend. This ensures the Posts API always gets a valid ID if it doesn't support empty strings.

## Proposed Changes

### Feature: Profile
Making the posts list robust and reactive.

#### [MODIFY] [ProfileViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileViewModel.kt)
- Convert `posts` to a `StateFlow` or use `flatMapLatest` on a `userId` flow.
- Emit `UiEvent.Refresh` when the `onPostCreated` event is received.
- Ensure the `posts` flow is refreshed when the user profile is successfully loaded if the ID was previously unknown.

#### [MODIFY] [ProfileScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileScreen.kt)
- Add loading and error indicators for the paging list.
- Ensure the refresh logic is correctly hooked up.
- Try to use the specialized `items` extension for `LazyPagingItems` again with the correct import.

## Verification Plan

### Manual Verification
1. Open your Profile. Verify that posts load eventually.
2. If there's an error, observe the snackbar or error message in the list.
3. Create a post and return to the Profile. Verify the list refreshes and shows the new post.
