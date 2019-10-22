package com.example.stunitastest.common

class Define {
    class Api {
        companion object {
            const val API_BASE_URL              = "https://dapi.kakao.com"
            const val API_PATH_SEARCH_IMAGE     = "/v2/search/image"

            const val API_PARAM_NAME_QUERY      = "query"
            const val API_PARAM_NAME_PAGE       = "page"

            const val API_PARAM_NAME_SIZE       = "size"

            const val API_PARAM_VALUE_SIZE      = 30

        }
    }

    class Extra {
        companion object {
            const val EXTRA_KEY_IMAGE_URI       = "imageUri"
        }
    }
}