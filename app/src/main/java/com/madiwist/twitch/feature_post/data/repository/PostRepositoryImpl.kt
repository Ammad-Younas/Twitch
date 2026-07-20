package com.madiwist.twitch.feature_post.data.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.domain.util.getFileName
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.data.data_source.paging.PostSource
import com.madiwist.twitch.feature_post.data.data_source.remote.PostApi
import com.madiwist.twitch.feature_post.data.data_source.remote.request.CreatePostRequest
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PostRepositoryImpl (
    private val api: PostApi,
    private val gson: Gson,
    private val appContext: Context
) : PostRepository {
    override val posts: Flow<PagingData<Post>>
        get() = Pager(PagingConfig(pageSize = Constants.PAGE_SIZE_POSTS)) {
            PostSource(api)
        }.flow

    override suspend fun createPost(
        description: String,
        imageUri: Uri
    ): SimpleResource {
        val request = CreatePostRequest(description = description)

        val file: File = withContext(Dispatchers.IO) {
            when (imageUri.scheme) {
                "file" -> File(imageUri.path ?: return@withContext null)
                else -> {
                    appContext.contentResolver.openFileDescriptor(imageUri, "r")
                        ?.let { fileDescriptor ->
                            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
                            val rawName = appContext.contentResolver.getFileName(imageUri)
                            val safeName = if (rawName.contains('.')) rawName else "$rawName.jpg"
                            val dest = File(appContext.cacheDir, safeName)
                            FileOutputStream(dest).use { out -> inputStream.copyTo(out) }
                            dest
                        }
                }
            }
        } ?: return Resource.Error(
            uiText = UiText.StringResource(R.string.file_not_found)
        )
        return try {
            val mimeType = when (file.extension.lowercase()) {
                "png"  -> "image/png"
                "webp" -> "image/webp"
                else   -> "image/jpeg"
            }
            val response = api.createPost(
                postData = MultipartBody.Part.createFormData("post_data", gson.toJson(request)),
                postImage = MultipartBody.Part.createFormData(
                    name = "post_image",
                    filename = file.name,
                    body = file.asRequestBody(mimeType.toMediaTypeOrNull()!!)
                )
            )
            if (response.success) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.unknown_error))
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_something_went_wrong)
            )
        }
    }
}