# Walkthrough - Forcing Social Fields in MongoDB

I have implemented a more robust solution to ensure that the social media fields (`gitHubUrl`, `instagramUrl`, `linkedInUrl`) are always present in your MongoDB documents, even when they are `null`.

## Changes Made

### 1. Backend (Ktor)
Modified the [User.kt](file:///D:/MaDi/Practice/Ktor/Twitch_Backend/Twitch/src/main/kotlin/data/models/User.kt) model:
- Removed the default `null` values for `gitHubUrl`, `instagramUrl`, and `linkedInUrl`.
- By removing the default values from the class definition, `kotlinx-serialization` is forced to treat them as explicit fields that must be encoded during serialization, even if they hold a `null` value.

Updated [UserService.kt](file:///D:/MaDi/Practice/Ktor/Twitch_Backend/Twitch/src/main/kotlin/service/UserService.kt):
- Updated the `createUser` method to explicitly provide all fields for the `User` constructor, including setting the social URLs to `null` and the counts to `0`.

## Verification Results

### Automated Tests
- ✅ Backend build successful (`gradlew build -x test`).

## Manual Verification Required
1. **RESTART YOUR BACKEND SERVER**: Ensure you are running the latest compiled code.
2. Create a new account ("Ali" or any other) in the app.
3. Refresh MongoDB Compass and check the new document.
4. You should now see all fields (`gitHubUrl`, `instagramUrl`, `linkedInUrl`, `followerCount`, `followingCount`, `postCount`, `skills`) explicitly present in the document.
