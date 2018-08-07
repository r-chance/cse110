package com.team29.cse110.team29dejaphoto.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.team29.cse110.team29dejaphoto.interfaces.DejaPhoto;
import com.team29.cse110.team29dejaphoto.interfaces.PhotoLoader;
import com.team29.cse110.team29dejaphoto.models.LocalPhoto;

import java.io.File;

/**
 * Created by David Duplantier on 5/8/17.
 */

/*
* This class implements the PhotoLoader interface; it provides methods to load photos from a phones
* storage, and return them as an array of LocalPhoto objects.
*/
public class DejaPhotoLoader implements PhotoLoader {


    private final String TAG = "DejaPhotoLoader";

    /*
     * PROJECTIONS enumerates the pieces of data we want to retrieve for each photo.
     * TITLE: String for the name of a photo (e.g. "IMG_123456789").
     * LATITUDE: double value of latitude.
     * LONGITUDE: double value of the longitude.
     * DATE_TAKEN: Time photo was taken, in units of milliseconds since January 1, 1970.
     */
    private final String[] PROJECTIONS = { MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.DATE_TAKEN };


    /* Indices of columns from query (same as order defined in PROJECTIONS) */
    private final int TITLE_INDEX      = 0;
    private final int LAT_INDEX        = 1;
    private final int LONG_INDEX       = 2;
    private final int DATE_ADDED_INDEX = 3;


    /* This is the Uri for the storage of all photos */
    private final Uri MEDIA_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    /* SharedPreferences for karma and release */

    /*
     * This method searches all photos retrieved from the phone's storage and returns them as an
     * array of LocalPhoto objects. This method is intended to be used only once during the app's
     * lifecycle - when the app first begins and needs to load all photos into the DisplayCycleMediator
     * object.
     */
    @Override
    public DejaPhoto[] getPhotosAsArray(Context context) {

        Log.d(TAG, "Entering getPhotosAsArray method");

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MEDIA_URI, PROJECTIONS, null, null, null);

        int numOfPhotos = cursor.getCount();

        DejaPhoto[] gallery = new DejaPhoto[numOfPhotos];

        int numPhotos = 0;

        int count = 0;

        while ( cursor.moveToNext() ) {

            String filename = cursor.getString(TITLE_INDEX) + ".jpg";
            //String absolutePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + filename;

            String absolutePathDejaPhotoCopied = Environment.getExternalStorageDirectory() + "/DejaPhotoCopied/dejaCopied" + filename;
            Log.d(TAG, "absolutepath of dejaphotocopied " + absolutePathDejaPhotoCopied);
            File fileDejaPhotoCopied = new File(absolutePathDejaPhotoCopied);
            Uri uriDejaPhotoCopied = Uri.fromFile(fileDejaPhotoCopied);

            String absolutePathDejaPhotoTaken = Environment.getExternalStorageDirectory() + "/DejaPhoto/" + filename;
            Log.d(TAG, "absolutepath of dejaphototaken " + absolutePathDejaPhotoTaken);
            File fileDejaPhotoTaken = new File(absolutePathDejaPhotoTaken);
            Uri uriDejaPhotoTaken = Uri.fromFile(fileDejaPhotoTaken);

            String absolutePathDejaPhotoFriends = Environment.getExternalStorageDirectory() + "/DejaPhotoFriends/" + filename;
            Log.d(TAG, "absolutepath of dejaphotofriends " + absolutePathDejaPhotoFriends);
            File fileDejaPhotoFriends = new File(absolutePathDejaPhotoFriends);
            Uri uriDejaPhotoFriends = Uri.fromFile(fileDejaPhotoFriends);

            // used to hold uri of photo we want to load
            Uri uri = null;

            //used to get custom location of photo if there is one
            SharedPreferences sp = context.getSharedPreferences("Deja_Preferences", Context.MODE_PRIVATE);

            if(fileDejaPhotoCopied.exists()) {
                uri = uriDejaPhotoCopied;
                Log.d(TAG, "Loading photo from photopicker: " + uriDejaPhotoCopied);

            } else if(fileDejaPhotoTaken.exists()) {
                uri = uriDejaPhotoTaken;
                Log.d(TAG, "Loading photo from in-app camera: " + uriDejaPhotoTaken);
            }
            if(uri != null && !sp.getBoolean("R_" + uri, false)) {
                gallery[count] = new LocalPhoto(uri,
                        cursor.getDouble(LAT_INDEX),
                        cursor.getDouble(LONG_INDEX),
                        cursor.getLong(DATE_ADDED_INDEX),
                        sp.getString(uri.toString(), ""));

                Log.d(TAG, "custom location is " + sp.getString(uri.toString(), ""));

                if(sp.getBoolean("K_" + uri, false)) {
                    gallery[count].addKarma();
                }

                numPhotos++;
            }
            count++;
        }

        cursor.close();

        Log.d(TAG, "Finished Loading, total number of photos loaded: " + numPhotos);

        return gallery;
    }

    /*
     * This method returns all photos added to the phone since the last call to getPhotosAsArray()
     * or getNewPhotosAsArray().
     */
    @Override
    public LocalPhoto[] getNewPhotosAsArray(Context context) {
        return new LocalPhoto[]{};
    }
}

