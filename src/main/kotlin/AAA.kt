object AAA {
    @JvmStatic
    fun main(args: Array<String>) {
//        "https://127.0.0.1/wb".doPost(B(11111), object : OnResult<A> {
//            override fun onError(errorMsg: String?) {
//                System.out.println("请求失败,$errorMsg")
//            }
//
//            override fun onSucc(t: A?) {
//                System.out.println(t)
//            }
//
//        })
        "https://127.0.0.1/wb".doGet(object : OnResult<A> {
            override fun onError(errorMsg: String?) {
                System.out.println("请求失败,$errorMsg")
            }

            override fun onSucc(t: A?) {
                System.out.println(t)
            }
        })
    }


    data class A(val code: Int, val message: String, val data: Any)
    data class B(val code: Int)

}