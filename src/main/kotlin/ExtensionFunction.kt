import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.net.ssl.HostnameVerifier


val mOkHttpClient by lazy {
    OkHttpClient().newBuilder().hostnameVerifier(HostnameVerifier { p0, p1 -> true }).build()
}

private val mGson by lazy {
    Gson()
}

/**
 * 是否是手机号
 */
fun String.isPhone() = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$".toRegex().matches(this)

/**
 * 是否是邮箱地址
 */
fun String.isEmail() = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?".toRegex().matches(this)

/**
 * 是否是身份证号码
 */
fun String.isIDCard() = "[1-9]\\d{16}[a-zA-Z0-9]".toRegex().matches(this)

/**
 * 是否是中文字符
 */
fun String.isChinese() = "^[\u4E00-\u9FA5]+$".toRegex().matches(this)

/**
 * 转换json字符串
 */
fun Any.toJsonStr(): String = mGson.toJson(this)

/**
 * 将json串转换为实体
 */
inline fun <reified T> String.convertObject(): T = Gson().fromJson(this, T::class.java)


/**
 * 网络请求 post请求 上传json串
 * [any] 任意对象 内部会转换城json串
 * [mOnResult] 请求结果回调
 */
inline fun <reified T> String.doPost(any: Any?, mOnResult: OnResult<T>?) {
    val data = any?.toJsonStr() ?: "{}"
    request(Request.Builder().post(data.toRequestBody(contentType = "application/json; charset=utf-8".toMediaTypeOrNull()))
            .url(this)
            .build(), mOnResult)
}

/**
 * 网络请求 get请求 请求参数拼接在url后
 * [mOnResult] 请求结果回调
 */
inline fun <reified T> String.doGet(mOnResult: OnResult<T>?) = request(Request.Builder().get()
        .url(this)
        .build(), mOnResult)

inline fun <reified T> request(request: Request, mOnResult: OnResult<T>?) {
    mOkHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            mOnResult?.onError(errMsg = e.localizedMessage)
        }

        override fun onResponse(call: Call, response: Response) {
            mOnResult?.onSucc(response.body?.string()?.convertObject<T>())
        }
    })
}

interface OnResult<T> {
    fun onError(errMsg: String?)
    fun onSucc(t: T?)
}

fun String.log() {
    System.out.println(this)
}





