package com.libit.wingspayroll;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.libit.wingspayroll.Network.ApiClient;
import com.libit.wingspayroll.Network.ApiServices;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentUpload extends AppCompatActivity {
    ImageView backbtn;
    TextView nametxt;
    SearchableSpinner Documentnamespinner;
    String DocumentName;
    ApiServices mService;
    List<String> documentname;
    List<String> documentid;
    Integer documentdata;
    ProgressBar progressbar;
    Button buttonSubmit;
    TextView buttonChoosePhoto;
    private int PICK_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ImageView ivImagePhoto;
    String uploadImagePhoto="No Image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        backbtn = findViewById(R.id.backbtn);
        nametxt = findViewById(R.id.nametxt);
        progressbar=findViewById(R.id.progressbar);
        Documentnamespinner=findViewById(R.id.DocumentListspinner);
        ivImagePhoto = findViewById(R.id.ivImagePhoto);
        buttonSubmit = findViewById(R.id.Btn_buttonSubmit);
        buttonChoosePhoto = findViewById(R.id.buttonChoosePhoto);
        progressbar.setVisibility(View.GONE);
        nametxt.setText("Document Upload");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Documentnamespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documentdata = Integer.valueOf(documentid.get(position));
                Snackbar.make(view, " EmpId " +documentdata+ " Selected ", Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialogImage();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(DocumentName.trim()))  {
                    Toast.makeText(DocumentUpload.this, "Please fill all credentials", Toast.LENGTH_SHORT).show();
                } else {
                    if(uploadImagePhoto.equalsIgnoreCase("No Image")){
                        Toast.makeText(DocumentUpload.this, "Please Choose image", Toast.LENGTH_SHORT).show();
                    }else{
                        String DocName=DocumentName.trim();

                        progressbar.setVisibility(View.VISIBLE);

//                        BannerImageUploadModel model =new BannerImageUploadModel();
//                        model.setRegId(UserRegId);
//                        model.setBannerName(DocName);
//                        model.setImage(uploadImagePhoto);
//                        ImageUpload(model);
//                        Toast.makeText(ImageUpload.this, "Image Upload successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private void getDocumentlist() {
        progressbar.setVisibility(View.VISIBLE);
        mService = ApiClient.getClient().create(ApiServices.class);
        Call<ResponseBody> userCall = mService.getUnit();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody user = response.body();
                try {
                    String data = user.string();

                    JSONObject responsejobj = new JSONObject(data);
                    String stauts = responsejobj.getString("Status_Code");
                    String message = responsejobj.getString("Message");

                    documentid   = new ArrayList<>();
                    documentname = new ArrayList<>();

                    if (stauts.equalsIgnoreCase("200")) {
                        JSONArray coursearray = responsejobj.getJSONArray("unitList");
                        for (int i = 0; i < coursearray.length(); i++) {
                            JSONObject dataobj = coursearray.getJSONObject(i);
                            String unitId = dataobj.getString("unitId");
                            String UnitName = dataobj.getString("UnitName");

                            documentid.add(unitId);
                            documentname.add(UnitName);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, documentname);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Documentnamespinner.setAdapter(adapter);
                        }
                        progressbar.setVisibility(View.GONE);
                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(DocumentUpload.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressbar.setVisibility(View.GONE);
                    e.printStackTrace();
                } catch (IOException e) {
                    progressbar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void showPictureDialogImage() {
        progressbar.setVisibility(View.VISIBLE);
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        String[] pictureDialogItems = {
                "Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallaryImage();
                                break;
                            case 1:
                                takePhotoFromCameraImage();
                                break;
                        }
                    }
                });

        pictureDialog.show();
    }

    public void choosePhotoFromGallaryImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePhotoFromCameraImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    ivImagePhoto.setVisibility(View.VISIBLE);
                    ivImagePhoto.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        bitmap = ImageResizer.reduceBitmapSize(bitmap, 307200);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        uploadImagePhoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    } catch (Exception ee) {
                        Toast.makeText(getApplicationContext(), "error_3" + ee, Toast.LENGTH_SHORT).show();
                        ee.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    photo = ImageResizer.reduceBitmapSize(photo, 307200);
                    ivImagePhoto.setVisibility(View.VISIBLE);
                    ivImagePhoto.setImageBitmap(photo);

                    Log.d("Camera Image-->", "onActivityResult: " + photo);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    progressbar.setVisibility(View.GONE);
                    uploadImagePhoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }
                break;
            //--------------------------------------------------------------------------
        }
    }


//    private void ImageUpload(documentUploadModel model) {
//        mService = ApiClient.getClient().create(ApiInterface.class);
//        Call<ResponseBody> userCall = mService.ImageUpload(model);
//        userCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                ResponseBody user = response.body();
//                try {
//                    String data = user.string();
//                    JSONObject responsejobj = new JSONObject(data);
//                    String stauts = responsejobj.getString("Status_Code");
//                    String message = responsejobj.getString("Message");
//
//                    if (stauts.equalsIgnoreCase("200")) {
//                        ivImagePhoto.setImageResource(0);
//                        ivImagePhoto.setVisibility(View.GONE);
//                        Txt_Title.getText().clear();
//                        Toast.makeText(getApplicationContext(), "Image Save Successfully "+message, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}