package com.inhatc.android_vision.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.inhatc.android_vision.R;

import java.io.ByteArrayOutputStream;

public class VisionApi extends AppCompatActivity {
    private FirebaseFunctions mFunctions;
    private double latitude;
    private double longitude;

    public void visionApi(Resources res) {
        mFunctions = FirebaseFunctions.getInstance();
        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), img);

        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.test2);
        mFunctions = FirebaseFunctions.getInstance();

        bitmap = scaleBitmapDown(bitmap, 640);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        // Create json request to cloud vision
        JsonObject request = new JsonObject();
// Add image to request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);
//Add features to the request
        JsonObject feature = new JsonObject();
        feature.add("maxResults", new JsonPrimitive(5));
        feature.add("type", new JsonPrimitive("LANDMARK_DETECTION"));
        JsonArray features = new JsonArray();
        features.add(feature);
        request.add("features", features);


        annotateImage(request.toString())
                .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
                    @Override
                    public void onComplete(@NonNull Task<JsonElement> task) {
                        if (task.isSuccessful()) {
                            System.out.println(task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray());
                            for (JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray()) {
                                JsonObject labelObj = label.getAsJsonObject();
                                String landmarkName = labelObj.get("description").getAsString();
                                String entityId = labelObj.get("mid").getAsString();
                                float score = labelObj.get("score").getAsFloat();
                                JsonObject bounds = labelObj.get("boundingPoly").getAsJsonObject();
                                // Multiple locations are possible, e.g., the location of the depicted
                                // landmark and the location the picture was taken.
                                for (JsonElement loc : labelObj.get("locations").getAsJsonArray()) {
                                    JsonObject latLng = loc.getAsJsonObject().get("latLng").getAsJsonObject();
                                    latitude = latLng.get("latitude").getAsDouble();
                                    longitude = latLng.get("longitude").getAsDouble();
                                }
                                String name = "이름 : " + landmarkName + '\n';
                                name += "아이디 : "+entityId + '\n';
                                name += "정확도 : "+score + '\n';
                                name += "bounds : "  + bounds.toString() + '\n';
                                name += "위도 : " + latitude + '\n';
                                name += "경도 : " + longitude + '\n';
                                System.out.println(name);

                            }
                        } else {
                            System.out.println("왜 안나와");
                        }
                    }
                });
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                    @Override
                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }
}
