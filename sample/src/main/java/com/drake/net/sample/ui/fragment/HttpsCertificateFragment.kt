package com.drake.net.sample.ui.fragment

import android.view.View
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.okhttp.setSSLCertificate
import com.drake.net.okhttp.trustSSLCertificate
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentHttpsCertificateBinding
import com.drake.net.utils.scopeNetLife
import com.drake.tooltip.toast
import okhttp3.OkHttpClient

class HttpsCertificateFragment :
    EngineFragment<FragmentHttpsCertificateBinding>(R.layout.fragment_https_certificate) {

    override fun initView() {
        binding.btnTrustCertificate.setOnClickListener(this::trustAllCertificate)
        binding.btnImportCertificate.setOnClickListener(this::importCertificate)
    }

    override fun initData() {
    }

    /**
     * 信任全部证书
     * 大部分情况下还是建议在Application中配置一次全局的证书
     */
    private fun trustAllCertificate(view: View) {
        scopeNetLife {
            binding.tvResponse.text = Get<String>("https://github.com/liangjingkanji/Net/") {
                // 构建新的客户端
                okHttpClient = OkHttpClient.Builder().trustSSLCertificate().build()
            }.await()
        }
    }

    /**
     * 导入私有证书
     */
    private fun importCertificate(view: View) {
        scopeNetLife {
            Get<String>("https://github.com/liangjingkanji/Net/") {
                // 使用现在客户端
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