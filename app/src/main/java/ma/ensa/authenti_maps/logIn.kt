package ma.ensa.authenti_maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class logIn : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var signupNow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.email)
        editTextPassword  = findViewById(R.id.password)
        buttonLogin  = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)
        signupNow= findViewById(R.id.signupNow)

        signupNow.setOnClickListener{
            val intent = Intent(this, signUp::class.java )
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener {
          //  progressBar.visibility = View.VISIBLE
            val email :String = editTextEmail.text.toString()
            val password :String = editTextPassword.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                  //  progressBar.visibility = View.GONE
                    if (it.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Authentication Successfully.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                        //finish()
                    } else {

                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }


        }
    }

}