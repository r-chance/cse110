package com.team29.cse110.team29dejaphoto;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team29.cse110.team29dejaphoto.activities.MainActivity;
import com.team29.cse110.team29dejaphoto.interfaces.DejaPhoto;
import com.team29.cse110.team29dejaphoto.models.RemotePhoto;
import com.team29.cse110.team29dejaphoto.utils.FirebaseMediator;
import com.team29.cse110.team29dejaphoto.utils.FirebasePhotosHelper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tyler on 6/6/17.
 */
@RunWith(AndroidJUnit4.class)
public class FirebasePhotosHelperTest {

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<MainActivity>(MainActivity.class);
    Bitmap b;

    public static SharedPreferences sp;
    public final String prefTag = "Test_Prefs";

    FirebaseMediator fm = new FirebaseMediator(null, null);


    FirebasePhotosHelper firebasePhotosHelper;

    DejaPhoto[] gallery = new RemotePhoto[10];

    @Before
    public void setUp(){

        b = BitmapFactory.decodeResource(main.getActivity().getResources(),
            R.drawable.custom_icon_med);
        for(DejaPhoto d: gallery) {
            d = new RemotePhoto(b, 0, 0, 0, Calendar.getInstance().getTimeInMillis(),false, null, "");
        }

        firebasePhotosHelper = new FirebasePhotosHelper();

    }

    @Test
    public void upload() throws Exception {

        for(DejaPhoto d: gallery) {
            firebasePhotosHelper.upload(d);
        }
    }

    @Test
    public void downloadFriends() throws Exception {

        List<DejaPhoto> friend = firebasePhotosHelper.downloadFriends();
    }

    @Test
    public void deleteMyPhotos() throws Exception {

    }

    @Test
    public void enableSharing() throws Exception {
        assertFalse(firebasePhotosHelper.isSharing());
        firebasePhotosHelper.enableSharing();
        assertTrue(firebasePhotosHelper.isSharing());
    }

    @Test
    public void disableSharing() throws Exception {

        firebasePhotosHelper.disableSharing();
        assertFalse(firebasePhotosHelper.isSharing());
        firebasePhotosHelper.enableSharing();
        assertTrue(firebasePhotosHelper.isSharing());
    }

}