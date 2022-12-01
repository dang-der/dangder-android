package com.viewpoint.dangder.data.remote

import android.content.Context
import android.net.Uri
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.DefaultUpload
import com.apollographql.apollo3.api.Upload
import com.viewpoint.UploadFileMutation
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.inject.Inject

class FileRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    private val context: Context,
) {
    suspend fun uploadFiles(images: Array<Uri>): ApolloResponse<UploadFileMutation.Data> {
        return withContext(Dispatchers.IO) {
            val files = images.map { uri ->
                val fd = try {
                    context.contentResolver.openFileDescriptor(uri, "r")
                } catch (e: FileNotFoundException) {
                    throw e
                }

                return@map fd?.let {
                    val inputStream = FileInputStream(it.fileDescriptor)
                    val source = inputStream.source().buffer().readByteArray()

                    DefaultUpload.Builder()
                        .fileName(System.currentTimeMillis().toString())
                        .content { s -> s.write(source) }
                        .contentLength(source.size.toLong())
                        .contentType("image/*")
                        .build() as Upload
                }
            }.filterNotNull()

            apolloClient.mutation(UploadFileMutation(files)).execute()
        }
    }
}