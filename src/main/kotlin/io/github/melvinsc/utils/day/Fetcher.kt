package io.github.melvinsc.utils.day

import misc.Settings
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Network interface.
 */
interface Fetcher {
    @Headers("Cookie: session=${Settings.SESSION_COOKIE}")
    @GET("{year}/day/{day}/input")
    fun fetchInput(@Path("year") year: Int, @Path("day") day: Int): Call<String>

    companion object {
        private const val PATH_TO_RESOURCES = "src/main/resources"

        private val cacheDir = Files.createDirectories(
            Paths.get(System.getProperty("java.io.tmpdir"), "adventofcode")
        )

        private val okhttp = OkHttpClient.Builder()
            .cache(Cache(cacheDir.toFile(), 100 * 1024 * 1024))
            .build()

        private val fetcher: Fetcher = Retrofit.Builder()
            .client(okhttp)
            .baseUrl("https://adventofcode.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(Fetcher::class.java)

        fun getInput(year: Int, day: Int): String {
            val dataDir = File("$PATH_TO_RESOURCES/input/year$year")
            val file = File("$PATH_TO_RESOURCES/input/year$year/data%02d.txt".format(day))

            return if (file.exists()) {
                file.readText()
            } else {
                val data = fetcher.fetchInput(year, day).execute().body().orEmpty().trim()
                dataDir.mkdirs()
                file.createNewFile()
                file.writeText(data)

                data
            }
        }
    }
}

