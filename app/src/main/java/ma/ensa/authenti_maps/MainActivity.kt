package ma.ensa.authenti_maps

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import ma.ensa.authenti_maps.Modal.users
import java.util.Arrays


class MainActivity : AppCompatActivity() {
    private lateinit var googleBtn: Button
    private lateinit var emailBtn: Button
    private lateinit var client: GoogleSignInClient
    //fb
    private lateinit var callbackManager : CallbackManager
    private lateinit var buttonFacebookLogin: Button
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val accessToken = AccessToken.getCurrentAccessToken()
        if(accessToken != null && !accessToken.isExpired){
            startActivity( Intent(this,MainActivity2::class.java ))

        }
        FacebookSdk.sdkInitialize(applicationContext);

        googleBtn = findViewById(R.id.googleBtn)
        emailBtn = findViewById(R.id.emailBtn)
        emailBtn.setOnClickListener {
            val intent = Intent(applicationContext, logIn::class.java)
            startActivity(intent)
        }
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)
        googleBtn.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent , 10001)
        }
        //facebook login

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://authenti-maps-default-rtdb.firebaseio.com/")
        // Initialize Facebook Login button

        callbackManager = CallbackManager.Factory.create()


        buttonFacebookLogin = findViewById(R.id.login_button)
        buttonFacebookLogin.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
            LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d(TAG, "facebook:onSuccess:$loginResult")
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        Log.d(TAG, "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d(TAG, "facebook:onError", error)
                    }
                },
            )
        }



    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10001){
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user = auth.currentUser!!

                            val user1 = users()

                            user1.userName = user.displayName.toString()
                            database.reference.child("users").child(user.uid).setValue(user1)
                            val ii = Intent(this,MainActivity2::class.java )
                            ii.putExtra("name" , user.displayName.toString())
                            startActivity(ii)
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }

                    }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google Sign-In failed. Error: ${e.statusCode} - ${e.localizedMessage}")
                Toast.makeText(this, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Exception during Google Sign-In: ${e.message}")
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }else{
            //facebook
            callbackManager.onActivityResult(requestCode, resultCode, data)

        }
    }

    override fun onStart() {
        super.onStart()
//        user = auth.currentUser!!
//        if(user != null ){
//           val intent1 =  Intent(this, MainActivity2::class.java)
//            intent1.putExtra("name" , user.displayName.toString())
//            startActivity(intent1)
//        }

        if(FirebaseAuth.getInstance().currentUser != null){
            val i = Intent(this,MainActivity2::class.java)
            startActivity(i)


        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    user = auth.currentUser!!

                    val user1 = users()

                    user1.userName = user.displayName.toString()
                    database.reference.child("users").child(user.uid).setValue(user1)
                    val ii = Intent(this,MainActivity2::class.java )
                    ii.putExtra("name" , user.displayName.toString())
                    startActivity(ii)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

}


