package kivi.ugran.com.core;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
public class PlatformUtils {

    public static final int SELECT_PHOTO = 100;
    public static final int TAKE_PHOTO = 101;

    public static void openGallery(@NonNull Activity activity) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public static Bitmap loadBitMapFromUri(@NonNull Activity activity, @NonNull Uri selectedImage) {
        Bitmap bitmap = null;
        Log.d("kivi", "PlatformUtils :- input uri is " + selectedImage.getPath());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
            if (bitmap == null) {
                Log.d("kivi", "PlatformUtils:- Bitmap is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] getBytesFromUri(@NonNull Activity activity, @NonNull Uri selectedImage) {
        byte[] bytesFromBitmap = null;
        Log.d("kivi", "PlatformUtils :- input uri is " + selectedImage.getPath());
        Bitmap bitmap = loadBitMapFromUri(activity, selectedImage);
        if (bitmap != null) {
            bytesFromBitmap = getBytesFromBitmap(bitmap);
            if (bytesFromBitmap != null) {
                Log.d("kivi", "PlatformUtils :- bytesFromBitmap is valid");
            }
        } else {
            Log.d("kivi", "PlatformUtils :- Bitmap is null");
        }
        return bytesFromBitmap;
    }

    public static byte[] getBytesFromBitmap(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    //https://androidkennel.org/android-camera-access-tutorial/
    private static File getOutputMediaFile() {
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "KiviProfile");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    public static void takePicture(@NonNull Activity context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        context.startActivityForResult(intent, TAKE_PHOTO);
    }

    public static Uri takePictureUri(@NonNull Activity context) {
        Uri file =
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        context.startActivityForResult(intent, TAKE_PHOTO);
        return file;
    }

    //https://stackoverflow.com/questions/34376796/android-sharedpreferences-save-bitmap
    public static void saveProfilePictureToPreference(@NonNull Context context, @NonNull Bitmap bitmap) {
        //Convert o base64 String and save to shared preference
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);

        if (encoded != null && !encoded.isEmpty()) {
            SharedPreferenceUtils.saveString(context, Constants.KiviPreferences.KEY_PROFILE_PICTURE, encoded);
        }
    }

    @Nullable public static Bitmap loadProfilePictureFromPreference(@NonNull Context context) {
        String encoded = SharedPreferenceUtils.loadString(context, Constants.KiviPreferences.KEY_PROFILE_PICTURE);
        Bitmap bitmap = null;
        byte[] imageAsBytes;
        if (encoded != null) {
            imageAsBytes = Base64.decode(encoded, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }
        return bitmap;
    }

    @Nullable public static String loadAssetFileAsString(@NonNull Context context, @NonNull String filename) {
        InputStream is = null;
        String textOutput = null;
        try {
            is = context.getResources().getAssets().open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (null != is) {
                textOutput = convertStreamToString(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textOutput;
    }

    public static String convertStreamToString(@NonNull InputStream is) throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    //public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;
    //private void checkPlayServices() {
    //    GoogleApiAvailability api = GoogleApiAvailability.getInstance();
    //    int code = api.isGooglePlayServicesAvailable(this);
    //    if (code == ConnectionResult.SUCCESS) {
    //        onActivityResult(REQUEST_GOOGLE_PLAY_SERVICES, Activity.RESULT_OK, null);
    //    } else if (api.isUserResolvableError(code) &&
    //            api.showErrorDialogFragment(this, code, REQUEST_GOOGLE_PLAY_SERVICES)) {
    //        // wait for onActivityResult call (see below)
    //    } else {
    //        Toast.makeText(this, api.getErrorString(code), Toast.LENGTH_LONG).show();
    //    }
    //}
}
