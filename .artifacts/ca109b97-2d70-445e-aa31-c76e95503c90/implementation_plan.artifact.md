# Fix Profile Image and Stats Loading

The user is experiencing issues where profile and banner images are not loading on the profile screen, and the stats (followers, following, posts) are showing incorrect (hardcoded) values.

## Analysis of the Issues

### 1. Images Not Loading (Coil 3 Network Dependency)
The project uses **Coil 3** (`io.coil-kt.coil3:coil-compose:3.5.0`). In Coil 3, the network engine is no longer included by default in the core/compose artifacts to support Kotlin Multiplatform. On Android, you must explicitly add the network engine dependency to load images from `http`/`https`.

### 2. Hardcoded Profile Stats
`ProfileHeaderSection.kt` contains hardcoded values for followers, following, and post counts within the `ProfileStats` call, ignoring the `User` object passed to it.

### 3. Backend URL Discrepancy
The MongoDB database contains URLs pointing to the `/posts/` directory for profile images (e.g., `http://10.0.2.2:8001/posts/79741234.png`), while the backend constants suggest they should be in `/profile_picture/`. However, the files are physically located in the `uploads/posts/` directory on the backend, so the current URLs in the database are "correct" for the current file structure but indicate a logical bug in the backend upload/storage process.

## Proposed Changes

### Android App (Frontend)

#### [MODIFY] [build.gradle.kts](file:///D:/MaDi/Practice/App_Development/Twitch/app/build.gradle.kts)
- Add `io.coil-kt.coil3:coil-network-okhttp` dependency to enable network image loading.

#### [MODIFY] [libs.versions.toml](file:///D:/MaDi/Practice/App_Development/Twitch/gradle/libs.versions.toml)
- Add `coil-network-okhttp` library definition.

#### [MODIFY] [ProfileHeaderSection.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/components/ProfileHeaderSection.kt)
- Update `ProfileStats` call to use the `user` parameter instead of hardcoded values.

#### [MODIFY] [ProfileStats.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/components/ProfileStats.kt)
- Correct the logic for showing the "Follow" button: it should be shown only if `isOwnProfile` is **false**.

#### [MODIFY] [ProfileScreen.kt](file:///D:/MaDi/Practice/App_Development/Twitch/app/src/main/java/com/madiwist/twitch/feature_profile/presentation/profile/ProfileScreen.kt)
- Fix the typo where `followerCount` was passed as `followingCountL`.

### Backend (Recommendations)

> [!TIP]
> To maintain a clean project structure, you should ensure that profile pictures are stored in the `uploads/profile_picture` directory and served via the `/profile_picture/` URL, rather than using the `posts` directory for everything.

## Verification Plan

### Automated Tests
- Build the project to ensure dependencies are resolved correctly.

### Manual Verification
1. Run the app in the emulator.
2. Navigate to a profile from the search screen.
3. Verify that the profile image and banner image load correctly.
4. Verify that the follower, following, and post counts match the data in the database (12, 7, 330 for Ammad).
