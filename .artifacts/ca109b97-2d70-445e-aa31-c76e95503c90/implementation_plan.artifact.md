# Ensure All User Fields are Present in MongoDB

The user is reporting that the `gitHubUrl`, `instagramUrl`, and `linkedInUrl` fields are missing from the MongoDB documents when a new user is created. This is because `kotlinx.serialization` (used by the MongoDB Kotlin driver) omits nullable fields with a default value of `null` during serialization by default.

## Proposed Changes

### Backend (Ktor)

#### [MODIFY] [User.kt](file:///D:/MaDi/Practice/Ktor/Twitch_Backend/Twitch/src/main/kotlin/data/models/User.kt)
- Add `@EncodeDefault(EncodeDefault.Mode.ALWAYS)` to `gitHubUrl`, `instagramUrl`, and `linkedInUrl`.
- This ensures that even if these fields are `null`, they will be explicitly stored as `BSON NULL` in MongoDB, making them visible in MongoDB Compass.
- Note: This requires the `kotlinx.serialization.ExperimentalSerializationApi`.

```kotlin
@Serializable
data class User(
    // ... other fields
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val gitHubUrl: String? = null,

    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val instagramUrl: String? = null,

    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val linkedInUrl: String? = null,
    // ...
)
```

## Verification Plan

### Automated Tests
- Build the backend project to ensure no compilation errors with the new annotations.

### Manual Verification
1. Create a new user through the app's sign-up flow.
2. Open MongoDB Compass and verify that the `users` collection contains the new document.
3. Check that `gitHubUrl`, `instagramUrl`, and `linkedInUrl` are present in the document and set to `null`.
