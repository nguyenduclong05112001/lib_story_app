package story.news.app.api

import com.google.gson.GsonBuilder
import story.news.app.api.APIConst.baseUrl
import story.news.app.api.APIConst.getData
import story.news.app.lib_model.StoryAPI
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface APIServer {
    companion object {
        val gson = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
        val api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIServer::class.java)
    }

    @GET(getData)
    suspend fun getnewStory(): Response<StoryAPI>
}