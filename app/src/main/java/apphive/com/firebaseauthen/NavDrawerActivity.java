package apphive.com.firebaseauthen;

/**
 * Created by Sunain on 1/1/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gino Osahon on 03/03/2017.
 */

// This class is a simple activity with NavigationDrawer
// we get data stored in sharedPrefference and display on the header view of the NavigationDrawer
public class NavDrawerActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    Context mContext = this;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView mFullNameTextView, mEmailTextView;
    private CircleImageView mProfileImageView;
    private String mUsername, mEmail;

    SharedPrefManager sharedPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        View header = navigationView.getHeaderView(0);

        mFullNameTextView = (TextView) header.findViewById(R.id.fullName);
        mEmailTextView = (TextView) header.findViewById(R.id.email);
        mProfileImageView = (CircleImageView) header.findViewById(R.id.profileImage);
        // create an object of sharedPreferenceManager and get stored user data
        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        String uri = sharedPrefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

        //Set data gotten from SharedPreference to the Navigation Header view
        mFullNameTextView.setText(mUsername);
        mEmailTextView.setText(mEmail);

        Picasso.with(mContext).load(mPhotoUri).placeholder(android.R.drawable.sym_def_app_icon).error(android.R.drawable.sym_def_app_icon).into(mProfileImageView);

        configureSignIn();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ConnectFragment()).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
//        Toast.makeText(this,"menu button pressed",Toast.LENGTH_LONG).show();
//
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){drawerLayout.closeDrawers();}
        else{drawerLayout.openDrawer(Gravity.LEFT);}
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){drawerLayout.closeDrawers();}
        else{drawerLayout.openDrawer(Gravity.LEFT);}
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    // Initialize and add Listener to NavigationDrawer
    public void initNavigationDrawer(){

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id){
                    case R.id.freebie:
                        Toast.makeText(getApplicationContext(),"Sarcasminator",Toast.LENGTH_SHORT).show();
                        setTitle("Sarcasminator");
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new BotFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.payment:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        setTitle("Sarcasminator");
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.content_frame, new SplashFragment()).commit();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.trip:
                        Toast.makeText(getApplicationContext(),"Trip",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        signOut();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.tips:
                        Toast.makeText(getApplicationContext(),"Tip",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

        //set up navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    // This method configures Google SignIn
    public void configureSignIn(){
// Configure sign-in to request the user's basic profile like name and email
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mAuth = FirebaseAuth.getInstance();
    }

    //method to logout
    private void signOut(){
        new SharedPrefManager(mContext).clear();
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(NavDrawerActivity.this, MainActivity.class);
                        startActivity(intent);
//                        Menu nav_Menu = navigationView.getMenu();
//                        nav_Menu.findItem(R.id.payment).setVisible(false);
                    }
                }
        );
    }
}