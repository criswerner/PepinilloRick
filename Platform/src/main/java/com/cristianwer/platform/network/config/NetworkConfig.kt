package com.cristianwer.platform.network.config

internal object NetworkConfig {
    const val CONNECT_TIMEOUT_SECONDS = 15L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L

    const val CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10 MB
    const val CACHE_DIR_NAME = "http_cache"
}