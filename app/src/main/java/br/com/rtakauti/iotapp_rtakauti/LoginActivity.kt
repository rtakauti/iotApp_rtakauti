package br.com.rtakauti.iotapp_rtakauti

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        botaoCriarConta.setOnClickListener {
            chamaTelaCriarConta()
        }
        botaoLogar.setOnClickListener {
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                            inputEmail.text.toString(),
                            inputSenha.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            vaiParaTelaPrincipal()
                        } else {
                            Toast.makeText(this@LoginActivity, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }

    private fun vaiParaTelaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun chamaTelaCriarConta() {
        startActivity(Intent(this, SignupActivity::class.java))
    }
}
