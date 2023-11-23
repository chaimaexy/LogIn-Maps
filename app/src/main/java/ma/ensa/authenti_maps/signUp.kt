package ma.ensa.authenti_maps

import android.content.Intent
import android.opengl.Visibility
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

class signUp : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonReg: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var loginNow: TextView
    private lateinit var confirmPassword : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.email)
        editTextPassword  = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        buttonReg  = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        loginNow= findViewById(R.id.loginNow)

        loginNow.setOnClickListener{
            val intent = Intent(this, logIn::class.java )
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {

           // progressBar.visibility = View.VISIBLE
            val email :String = editTextEmail.text.toString()
            val password :String = editTextPassword.text.toString()
            val confirmedPassword :String = confirmPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmedPassword.isNotEmpty() ){
                if(password == confirmedPassword){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, logIn::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this, it.exception.toString(),
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
            }







        }

    }


}