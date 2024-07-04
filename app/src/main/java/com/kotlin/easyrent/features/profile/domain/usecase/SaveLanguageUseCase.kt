package com.kotlin.easyrent.features.profile.domain.usecase

import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.prefrences.LanguagePreferencesManager
import javax.inject.Inject

class SaveLanguageUseCase @Inject constructor(
    private val languagePreferencesManager: LanguagePreferencesManager
) {
    suspend operator fun invoke(language: Language) {
        languagePreferencesManager.saveLanguage(language)
    }
}