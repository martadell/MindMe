package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class PopUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        ImageView foto = (ImageView) findViewById(R.id.big_image);

        Intent bigImage = getIntent();
        String path = bigImage.getStringExtra("picture");
        foto.setImageURI(Uri.parse(path));

    }
}
