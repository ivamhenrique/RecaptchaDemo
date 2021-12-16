package br.com.henriktech.recaptchademo

import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNet.SafetyNetApi


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val SITE_KEY = "6LfQQqgdAAAAAE0FGa60dpO56oWF0l-14zzyJh_R"
    private val SECRET_KEY = "6LfQQqgdAAAAAFSA3Q1Pc2GMRuMCNtlhegC9pDUM"
    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var chekbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectGoogleApi()

        chekbox = findViewById(R.id.check_box)
        chekbox.setOnClickListener {
            SafetyNetApi.verifyWithRecaptcha(googleApiClient, SITE_KEY)
                .setResultCallback { result ->
                    val status: Status = result.status
                    chekbox.isChecked = status.isSuccess
                }
        }
    }

    override fun onConnected(@Nullable bundle: Bundle?) {
        Toast.makeText(this, "onConnected()", Toast.LENGTH_LONG).show()
    }

    override fun onConnectionSuspended(i: Int) {
        Toast.makeText(
            this,
            "onConnectionSuspended: $i",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(
            this,
            """
            onConnectionFailed():
            ${connectionResult.errorMessage}
            """.trimIndent(),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun connectGoogleApi() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(SafetyNet.API)
            .addConnectionCallbacks(this@MainActivity)
            .addOnConnectionFailedListener(this@MainActivity)
            .build()
        googleApiClient.connect()
    }
}