package br.com.henriktech.recaptchademo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.hcaptcha.sdk.*
import com.hcaptcha.sdk.tasks.OnFailureListener
import com.hcaptcha.sdk.tasks.OnSuccessListener


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var chekbox: CheckBox
    private lateinit var editText: EditText
    private lateinit var recaptcha: LinearLayout
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chekbox = findViewById(R.id.check_box)
        editText = findViewById(R.id.editText)
        recaptcha = findViewById(R.id.recaptcha)
        button = findViewById(R.id.button)

        editText.doAfterTextChanged {
            if (it!!.length == 16) {
                hiddenKeyboard(editText)
                if (CardUtil.isValidPan(editText.text.toString())) {
                    button.isEnabled = true
                    onClickHCaptcha(HCaptchaSize.NORMAL)
                }
            }
        }
        editText.setOnClickListener {
            editText.setText("")
            button.isEnabled = false
            recaptcha.visibility = View.GONE
            onResume()
        }

        button.setOnClickListener {
            if (CardUtil.isValidPan(editText.text.toString())) {
                Toast.makeText(this, "Cartão válido", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Cartão inválido", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun hiddenKeyboard(v: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun onClickHCaptcha(hCaptchaSize: HCaptchaSize?) {
        val YOUR_API_SITE_KEY = "c7d19c09-8bea-4883-8c16-1259932b0c56"
        val config: HCaptchaConfig = HCaptchaConfig.builder()
            .siteKey(YOUR_API_SITE_KEY)
            .apiEndpoint("https://js.hcaptcha.com/1/api.js")
            .size(hCaptchaSize)
            .loading(true)
            .build()
        HCaptcha.getClient(this).verifyWithHCaptcha(config)
            .addOnSuccessListener(OnSuccessListener<HCaptchaTokenResponse>() {
                fun onSuccess(response: HCaptchaTokenResponse) {
                    response.tokenResult
                }
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.d(
                    TAG,
                    "hCaptcha failed: " + e.message + "(" + e.statusCode + ")"
                )
            })
    }
}