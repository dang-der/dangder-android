package com.viewpoint.dangder.domain.usecase.like

import com.viewpoint.dangder.domain.repository.LikeRepository
import com.viewpoint.dangder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateLikeUseCase @Inject constructor(
    private val likeRepository: LikeRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke( receiveDogId : String): Boolean {
        try {
            val myDogId = settingsRepository.getDogId().first()
            return likeRepository.createLike(sendDogId = myDogId, receiveDogId = receiveDogId)
        }catch (e : Exception){
            throw e
        }
    }
}