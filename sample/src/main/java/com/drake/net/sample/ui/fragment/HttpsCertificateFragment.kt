package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.okhttp.setSSLCertificate
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import com.drake.tooltip.toast
import kotlinx.android.synthetic.main.fragment_https_certificate.*
import okhttp3.OkHttpClient

class HttpsCertificateFragment : Fragment(R.layout.fragment_https_certificate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_trust_certificate.setOnClickListener(this::trustAllCertificate)
        btn_import_certificate.setOnClickListener(this::importCertificate)
    }

    /**
     * 信任全部证书
     */
    fun trustAllCertificate(view: View) {
        scopeNetLife {
            tv_response.text = Get<String>("https://github.com/") {
                okHttpClient = OkHttpClient.Builder().build()
            }.await()
        }
    }

    /**
     * 导入私有证书
     */
    fun importCertificate(view: View) {
        scopeNetLife {
            Get<String>("https://github.com/") {
                setClient {
                    val privateCertificate = resources.assets.open("https.certificate")
                    setSSLCertificate(privateCertificate)
                }
            }.await()
        }.catch {
            toast("作者没有证书, 只是演示代码, O(∩_∩)O哈哈~")
        }
    }

}