# Remove Paging3 and Implement Manual Paging

This plan describes how to replace the `androidx.paging` (Paging3) implementation with a manual paging approach across the Activities, Posts, and Profile features.

## Proposed Changes

### [Core Utilities]

#### [NEW] [Paginator.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/core/util/Paginator.kt)
- Add a generic `Paginator` interface and `DefaultPaginator` implementation to manage paging state and loading logic.

---

### [Activity Feature]

#### [MODIFY] [ActivityApi.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/data/remote/ActivityApi.kt)
- No changes needed to the API interface as it already supports `page` and `pageSize`.

#### [DELETE] [ActivitySource.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/data/paging/ActivitySource.kt)
- Remove the Paging3 `PagingSource` implementation.

#### [MODIFY] [ActivityRepository.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/domain/repository/ActivityRepository.kt)
- Change `activities: Flow<PagingData<Activity>>` to `suspend fun getActivities(page: Int, pageSize: Int): Resource<List<Activity>>`.

#### [MODIFY] [ActivityRepositoryImpl.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/data/repository/ActivityRepositoryImpl.kt)
- Implement `getActivities` using `ActivityApi` and manual error handling.

#### [MODIFY] [GetActivityUseCase.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/domain/use_case/GetActivityUseCase.kt)
- Change return type to `suspend fun invoke(page: Int, pageSize: Int): Resource<List<Activity>>`.

#### [MODIFY] [ActivityState.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/presentation/ActivityState.kt)
- Add `activities: List<Activity>`, `isLoading`, `endReached`, and `page`.

#### [MODIFY] [ActivityViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/presentation/ActivityViewModel.kt)
- Replace Paging3 `Flow<PagingData>` with manual paging using `DefaultPaginator`.
- Add `loadNextActivities()` method.

#### [MODIFY] [ActivityScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_activity/presentation/ActivityScreen.kt)
- Replace `collectAsLazyPagingItems` with manual list observation.
- Trigger `loadNextActivities()` when scrolling to the end.

---

### [Post Feature]

#### [DELETE] [PostSource.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/data/paging/PostSource.kt)
- Remove the Paging3 `PagingSource` implementation.

#### [MODIFY] [PostRepository.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/domain/repository/PostRepository.kt)
- Change `posts: Flow<PagingData<Post>>` to `suspend fun getPostsForFollows(page: Int, pageSize: Int): Resource<List<Post>>`.

#### [MODIFY] [PostRepositoryImpl.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/data/repository/PostRepositoryImpl.kt)
- Implement `getPostsForFollows` using `PostApi`.

#### [MODIFY] [GetPostsForFollowsUseCase.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/domain/use_case/GetPostsForFollowsUseCase.kt)
- Change return type to `suspend fun invoke(page: Int, pageSize: Int): Resource<List<Post>>`.

#### [MODIFY] [MainFeedState.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedState.kt)
- Add `posts: List<Post>`, `isLoading`, `endReached`, and `page`.

#### [MODIFY] [MainFeedViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedViewModel.kt)
- Replace Paging3 `Flow<PagingData>` with manual paging using `DefaultPaginator`.

#### [MODIFY] [MainFeedScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_post/presentation/main_feed/MainFeedScreen.kt)
- Replace `collectAsLazyPagingItems` with manual list observation.

---

### [Profile Feature]

#### [MODIFY] [ProfileRepository.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/domain/repository/ProfileRepository.kt)
- Change `getPostsPaged(userId: String): Flow<PagingData<Post>>` to `suspend fun getPosts(userId: String, page: Int, pageSize: Int): Resource<List<Post>>`.

#### [MODIFY] [ProfileRepositoryImpl.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/data/repository/ProfileRepositoryImpl.kt)
- Implement `getPosts` using `PostApi`.

#### [MODIFY] [GetPostsForProfileUserCase.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/domain/user_case/GetPostsForProfileUserCase.kt)
- Change return type to `suspend fun invoke(userId: String, page: Int, pageSize: Int): Resource<List<Post>>`.

#### [MODIFY] [ProfileState.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileState.kt)
- Add `posts: List<Post>`, `isLoadingPosts`, `endReached`, and `page`.

#### [MODIFY] [ProfileViewModel.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileViewModel.kt)
- Replace Paging3 `Flow<PagingData>` with manual paging using `DefaultPaginator`.

#### [MODIFY] [ProfileScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileScreen.kt)
- Replace `collectAsLazyPagingItems` with manual list observation.

## Verification Plan

### Manual Verification
- Verify that the Main Feed loads posts as you scroll down.
- Verify that the Activity screen loads activities as you scroll down.
- Verify that the Profile screen loads user posts as you scroll down.
- Ensure that refresh events (e.g., creating a post) correctly refresh the list.
