package com.android.oedermealapp.net

import com.android.frameworktool.network.NetWorkManager.Companion.getNetworkManager
import retrofit2.Retrofit

class NetWork {
    var networkServices =
        netWorkServices.create(
            NetWorkServices::class.java
        )

    companion object {
        private var networkManager: Retrofit? = null
        private var network: NetWork? = null
        val netWork: NetWork
            get() = if (network == null) {
                network = NetWork()
                network!!
            } else {
                network!!
            }

        private val netWorkServices: Retrofit
            get() = if (networkManager == null) {
                networkManager =
                    getNetworkManager("http://39.99.210.2:8080/")
                networkManager!!
            } else {
                networkManager!!
            }
    }
}