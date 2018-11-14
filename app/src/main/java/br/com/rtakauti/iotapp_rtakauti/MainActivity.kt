package br.com.rtakauti.iotapp_rtakauti

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private lateinit var userId: String

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        etSerialNumber.setText("ZrjeAW4jgf9LdqW5oBBBsNHp7sXATLBR")

        ivStatus.setOnClickListener {
            if (ivStatus.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.desligada)?.constantState) {
                onOff(1)
            } else {
                onOff(0)
            }
        }

        btRegistrarDevice.setOnClickListener {
            onOff(0)
            escutarMudancaNoFirebase()
        }

        btLogout.setOnClickListener {
            mAuth.signOut()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }

    private fun escutarMudancaNoFirebase() {

        FirebaseDatabase.getInstance().getReference("Coisas")
                .child(etSerialNumber.text.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val coisa = dataSnapshot.getValue(CoisaDigital::class.java)
                        when (coisa?.valor) {
                            0 -> {
                                ivStatus.visibility = View.VISIBLE
                                ivStatus.setImageDrawable(
                                        ContextCompat.getDrawable(
                                                this@MainActivity,
                                                R.drawable.desligada
                                        )
                                )
                            }
                            1 -> {
                                ivStatus.visibility = View.VISIBLE
                                ivStatus.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ligada))
                            }
                            else -> {
                                ivStatus.visibility = View.GONE
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun onOff(valor: Int) {
        FirebaseDatabase.getInstance().getReference("Coisas")
                .child(etSerialNumber.text.toString())
                .setValue(CoisaDigital("LED", valor))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Comando executado com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
