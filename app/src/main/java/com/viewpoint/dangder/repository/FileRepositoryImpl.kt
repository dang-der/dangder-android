package com.viewpoint.dangder.repository

import android.net.Uri
import com.viewpoint.dangder.data.remote.FileRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileRemoteDataSource: FileRemoteDataSource
) :FileRepository {
    override suspend fun uploadFiles(images: Array<Uri>): List<String> {
        val response = fileRemoteDataSource.uploadFiles(images)

        if (response.hasErrors()){
            Timber.e(response.errors?.first().toString())
            throw Exception(response.errors?.first()?.message)
        }
        val urls = response.data?.uploadFile ?: throw Exception("이미지 업로드에 실패했습니다.")
        return urls
    }
}