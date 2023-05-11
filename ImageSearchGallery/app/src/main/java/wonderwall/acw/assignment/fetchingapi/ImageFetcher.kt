package wonderwall.acw.assignment.fetchingapi

import android.util.Log
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import wonderwall.acw.assignment.BuildConfig.apiKey

data class ImageSearchResult(
    val thumbnail_url: String,
    val datetime: String,
    var isDownloaded: Boolean
)

data class VideoSearchResult(val thumbnail: String, val datetime: String)
data class ImageSearchResponse(val documents: List<ImageSearchResult>)
data class VideoSearchResponse(val documents: List<VideoSearchResult>)

private const val REST_API_KEY =apiKey
private const val BASE_URL = "https://dapi.kakao.com/"

interface KakaoApiService {
    @GET("v2/search/image")
    suspend fun searchImages(
        @Header("Authorization") authorization: String,
        @Query("query") query: String
    ): Response<ImageSearchResponse>

    @GET("v2/search/vclip")
    suspend fun searchVideos(
        @Header("Authorization") authorization: String,
        @Query("query") query: String
    ): Response<VideoSearchResponse>
}


val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val kakaoApiService = retrofit.create(KakaoApiService::class.java)


class ImageFetcher() {
    suspend fun searchImages(query: String): List<ImageSearchResult> {
        val response = kakaoApiService.searchImages("KakaoAK $REST_API_KEY", query)
        Log.d("searchImages", response.toString())
        Log.d("searchImages", response.body()?.documents.toString())
        return if (response.isSuccessful) {
            response.body()?.documents ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun searchVideos(query: String): List<VideoSearchResult> {
        val response = kakaoApiService.searchVideos("KakaoAK $REST_API_KEY", query)
        runBlocking {

        }
        return if (response.isSuccessful) {
            response.body()?.documents ?: emptyList()
        } else {
            emptyList()
        }
    }

    companion object {
        private var INSTANCE: ImageFetcher? = null
        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = ImageFetcher()
            }
        }

        fun get(): ImageFetcher {
            return INSTANCE ?: throw IllegalStateException("ImageFetcher must be initialize")
        }
    }

}
