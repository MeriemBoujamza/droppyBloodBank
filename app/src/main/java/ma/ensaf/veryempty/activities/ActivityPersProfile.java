package ma.ensaf.veryempty.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ma.ensaf.veryempty.R;
import ma.ensaf.veryempty.adapters.PostsAdapter;
import ma.ensaf.veryempty.databinding.ActivityPersProfileBinding;
import ma.ensaf.veryempty.models.Posts;
import ma.ensaf.veryempty.models.Users;
import ma.ensaf.veryempty.utils.Constants;
import ma.ensaf.veryempty.utils.PreferenceManager;

public class ActivityPersProfile extends BaseActivity {

    private static final String TAG = ActivityPersProfile.class.getSimpleName();
    ActivityPersProfileBinding binding;
    private PreferenceManager preferenceManager;


    public static void start(Context context, Users obj) {
        Intent intent = new Intent(context, ActivityPersProfile.class);
        intent.putExtra(ma.ensaf.veryempty.data.Constants.USER_EXTRA_OBJECT, obj);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pers_profile);
        preferenceManager = new PreferenceManager(getApplicationContext());

        initToolbar(binding.toolbar,true);

        // show the data in the views
        initViews();
        setListeners();
    }

    private void setListeners() {
        TextView EditBtn = findViewById(R.id.pers_donor_type_text_view);
        EditBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ActivityEditPersInfo.class));
            finish();
                }
        );
    }

    private void initViews() {
        binding.usernameTextView.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.persContentTop.persImageView.setImageBitmap(bitmap);
        binding.persContentTop.persLocationTextView.setText(preferenceManager.getString(Constants.KEY_CITY));
        binding.persContentTop.persDescriptionTextView.setText(activityContext.getString(R.string.medium_lorem_ipsum));

    }

}