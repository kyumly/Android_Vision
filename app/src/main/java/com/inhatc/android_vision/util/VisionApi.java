package com.inhatc.android_vision.util;

import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

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
import com.inhatc.android_vision.View.visionView;

import java.io.ByteArrayOutputStream;

public class VisionApi {
    private FirebaseFunctions mFunctions;

    private static double latitude;
    private static double longitude;
    private static String landmark_name;
    public static boolean confirm, chkApi =false;


    public static double getLatitude() {
        return latitude;
    }
    public static double getLongitude() {
        return longitude;
    }
    public static String getLandmark_name() {
        return landmark_name;
    }


    public void visionApi (Bitmap bitmap) {
        mFunctions = FirebaseFunctions.getInstance();

        bitmap = scaleBitmapDown(bitmap, 640);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        // JSON 호출 형태
        JsonObject request = new JsonObject();
        
        // 이미지 request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);

        //속성값 추가하기
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
                        String name = "";
                        if (task.isSuccessful()) {
                            System.out.println(task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray());
                            JsonArray jsonArray = task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray();
                            chkApi = false;
                            if(jsonArray.isEmpty()){
                                longitude = 0;
                                latitude =0;
                                confirm =false;
                                chkApi = true;
                                return;
                            }else{
                                for (JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray()) {
                                    JsonObject labelObj = label.getAsJsonObject();
                                    String landmarkName = labelObj.get("description").getAsString();
                                    landmark_name = landmarkName;
                                    String entityId = labelObj.get("mid").getAsString();
                                    float score = labelObj.get("score").getAsFloat();
                                    JsonObject bounds = labelObj.get("boundingPoly").getAsJsonObject();

                                    for (JsonElement loc : labelObj.get("locations").getAsJsonArray()) {
                                        JsonObject latLng = loc.getAsJsonObject().get("latLng").getAsJsonObject();
                                        latitude = latLng.get("latitude").getAsDouble();
                                        longitude = latLng.get("longitude").getAsDouble();
                                    }
                                }
                                chkApi = true;
                                confirm = true;
                                return;
                            }
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
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }


}