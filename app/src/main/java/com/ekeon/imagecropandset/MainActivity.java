package com.ekeon.imagecropandset;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.sd_Image) ImageView image;
  @Bind(R.id.sd_Image2) ImageView image2;
  @Bind(R.id.tv_geturi) TextView getUri;

  Uri fileUri;

  @OnClick(R.id.btn_getImage)
  void getImage() {
    image.setImageURI(null);
    Crop.pickImage(this);
  }

  @OnClick(R.id.sd_Image)
  void getImage2() {
    image2.setImageURI(fileUri);
    getUri.setText(fileUri.toString());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  private void beginCrop(Uri source) {
    Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
    Crop.of(source, destination).asSquare().start(this);
    getUri.setText(destination.toString());
  }

  private void handleCrop(int resultCode, Intent result) {
    if (resultCode == RESULT_OK) {
      image.setImageURI(Crop.getOutput(result));
      fileUri = Crop.getOutput(result);
    } else if (resultCode == Crop.RESULT_ERROR) {
      Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent result) {
    if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
      beginCrop(result.getData());
    } else if (requestCode == Crop.REQUEST_CROP) {
      handleCrop(resultCode, result);
    }
  }

}
