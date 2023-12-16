package ma.ensa.authenti_maps

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity2 : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private lateinit var btn : Button
    private lateinit var textView : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var callbackManager: CallbackManager



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //
        val drawer: DrawerLayout =findViewById(R.id.drawerLayout)
        val Navigation: NavigationView =findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        Navigation.bringToFront()
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        Navigation.setNavigationItemSelectedListener(this)

        //
        textView = findViewById(R.id.textView)

        val name = intent.getStringExtra("name")
        textView.text = name

        btn = findViewById(R.id.button)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        btn.setOnClickListener {
                auth.signOut()
                LoginManager.getInstance().logOut()



            val i = Intent(this , MainActivity::class.java)
            startActivity(i)
        }


        user = auth.currentUser!!
        if(name == null){
            textView.text = user.email
        }
//        else
//        {
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map -> {
                val intent = Intent(this,MapsEnsa::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                auth.signOut()
                LoginManager.getInstance().logOut()



                val i = Intent(this , MainActivity::class.java)
                startActivity(i)
            }


        }
        val drawer: DrawerLayout = findViewById(R.id.drawerLayout)
        drawer.closeDrawer(GravityCompat.START)

        return false
    }
}