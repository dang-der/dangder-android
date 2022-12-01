package com.viewpoint.dangder.repository

import android.net.Uri

interface FileRepository {
    suspend fun uploadFiles(images : Array<Uri>): List<String>
}