package utils

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import utils.ApiService.userInfo
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

object HttpClient {

    //默认Json
    val defJson = Json { ignoreUnknownKeys = true }

    /**
     * GET
     */
    suspend inline fun <reified T> get(url: String, params: String = ""): T {
        return parseResult(doRequest("$url$params", null))
    }

    /**
     * POST
     */
    @Suppress("UNCHECKED_CAST")
    suspend inline fun <reified T> post(url: String, params: Any): T {
        val paramsJson = defJson.encodeToString(params::class.serializer() as KSerializer<Any>, params)
        return parseResult(doRequest(url, paramsJson))
    }

    /**
     * 请求
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun doRequest(url: String, params: String?): String = withContext(IO) {
        var urlConn: HttpURLConnection? = null
        try {
            //替换域名
            val requestUrl = url
                .replace("{__weburl__}", "${userInfo?.webUrl}")
                .replace("{__serviceurl__}", "${userInfo?.serviceUrl}")
            //打开连接
            urlConn = URL(requestUrl).openConnection() as HttpURLConnection
            urlConn.apply {
                //设置读取超时10s
                readTimeout = 10_000
                //设置连接超时10s
                connectTimeout = 10_000
                //设置请求属性
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Cookie", "UserID=${userInfo?.accessCookie}")
                setRequestProperty("Authorization", "Bearer ${userInfo?.accessToken}")
                //判断请求方法
                if (params != null) {
                    doOutput = true
                    requestMethod = "POST"
                    //写入数据
                    outputStream.use {
                        it.write(params.toByteArray())
                    }
                }
            }
            //返回响应数据
            return@withContext if (urlConn.contentEncoding == "gzip") {
                GZIPInputStream(urlConn.inputStream)
            } else {
                urlConn.inputStream
            }.reader().use {
                it.readText()
            }
        } finally {
            urlConn?.disconnect()
        }
    }

    inline fun <reified T> parseResult(result: String): T {
        return if (T::class == String::class) {
            result as T
        } else {
            defJson.decodeFromString(result)
        }
    }
}