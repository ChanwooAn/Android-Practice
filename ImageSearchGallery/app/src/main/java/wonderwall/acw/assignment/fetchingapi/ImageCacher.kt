package wonderwall.acw.assignment.fetchingapi

import android.content.Context
import android.util.LruCache
import wonderwall.acw.assignment.utils.isNetworkConnected


private const val maxCacheSize = 1000000
private val cache: LruCache<String, List<ImageSearchResult>> = LruCache(maxCacheSize)

class ImageCacher() {
    suspend fun search(query: String, context: Context): List<ImageSearchResult> {
        if (!isNetworkConnected(context)) {
            cache[query]?.let { cachedResults ->
                return cachedResults
            }
            return emptyList()
        }//network 예외처리

        cache[query]?.let { cachedResults ->
            return cachedResults
        }
        val images = ImageFetcher.get().searchImages(query)
        val videos = ImageFetcher.get().searchVideos(query)

        val combined =
            (images + videos.map { ImageSearchResult(it.thumbnail, it.datetime, false) })
                .sortedByDescending { it.datetime }

        cache.put(query, combined)

        return combined
    }

    companion object {
        private var INSTANCE: ImageCacher? = null
        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = ImageCacher()
            }
        }

        fun get(): ImageCacher {
            return INSTANCE ?: throw IllegalStateException("ImageCacher must be initialize")
        }
    }

}
