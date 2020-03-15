package com.lib.utils.android

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.*


/**
 *
 *
 * @author: Admin.
 * @date  : 2020-03-03.
 */
object LanguageUtils {

    /**
     * 7.0系统之下版本切换
     */
    @Suppress("DEPRECATION")
    fun changeLanguage(context: Context, language: String) {
        if (language.isNullOrBlank()) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return
        }
        val resources = context.resources
        val configuration = resources.configuration
        //获取想要切换的语言类型
        //获取想要切换的语言类型
        val locale: Locale = getLocaleLanguage(language, Locale.ENGLISH)
        configuration.locale = locale
        // updateConfiguration
        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
    }

    private fun getLocaleLanguage(
        language: String,
        defaultLocale: Locale = Locale.SIMPLIFIED_CHINESE
    ): Locale {
        var locale = defaultLocale
        when (language) {
            LanguageType.CHINESE.language -> {
                locale = Locale.SIMPLIFIED_CHINESE
            }
            LanguageType.CHINESE_TW.language -> {
                locale = Locale.TAIWAN
            }
            LanguageType.CHINESE_HK.language -> {
                locale = Locale.TAIWAN
            }
            LanguageType.ENGLISH.language -> {
                locale = Locale.ENGLISH
            }
        }
        return locale
    }

    /**
     * 7.0系统之上版本切换
     */
    fun attachBaseContext(context: Context?, language: String): Context? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && context != null) {
            updateResources(context, language)
        } else {
            context
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateResources(
        context: Context,
        language: String
    ): Context {
        val locale: Locale = this.getLocaleLanguage(language, Locale.ENGLISH)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.locales = LocaleList(locale)
        return context.createConfigurationContext(configuration)
    }
}