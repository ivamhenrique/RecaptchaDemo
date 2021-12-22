package br.com.henriktech.recaptchademo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNet.SafetyNetApi

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val SITE_KEY = "6LfQQqgdAAAAAE0FGa60dpO56oWF0l-14zzyJh_R"
    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var chekbox: CheckBox
    private lateinit var editText: EditText
    private lateinit var recaptcha: LinearLayout
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectGoogleApi()

        chekbox = findViewById(R.id.check_box)
        editText = findViewById(R.id.editText)
        recaptcha = findViewById(R.id.recaptcha)
        button = findViewById(R.id.button)

        editText.doAfterTextChanged {
            if (it!!.length == 16) {
                hiddenKeyboard(editText)
                if (CardUtil.isValidPan(editText.text.toString())) {
                    button.isEnabled = true
                } else {
                    recaptcha.visibility = View.VISIBLE
                }
            }
        }
        editText.setOnClickListener {
            editText.setText("")
            button.isEnabled = false
            recaptcha.visibility = View.GONE
            onResume()
        }

        chekbox.setOnClickListener {
            SafetyNetApi.verifyWithRecaptcha(googleApiClient, SITE_KEY)
                .setResultCallback { result ->
                    val status: Status = result.status
                    chekbox.isChecked = if (status.isSuccess) {
                        button.isEnabled = true
                        true
                    } else {
                        false
                    }
                }
        }

        button.setOnClickListener {
            if(CardUtil.isValidPan(editText.text.toString())){
                Toast.makeText(this, "Cartão válido", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this, "Cartão inválido", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onConnected(@Nullable bundle: Bundle?) {
        Log.i("LOG", "onConnected")
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i("LOG", "onConnectionSuspended: $i")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i("LOG", "onConnectionFailed():${connectionResult.errorMessage}".trimIndent())
    }

    private fun connectGoogleApi() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(SafetyNet.API)
            .addConnectionCallbacks(this@MainActivity)
            .addOnConnectionFailedListener(this@MainActivity)
            .build()
        googleApiClient.connect()
    }

    private fun hiddenKeyboard(v: View) {
       val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }
}