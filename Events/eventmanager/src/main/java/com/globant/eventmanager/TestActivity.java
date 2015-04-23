package com.globant.eventmanager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;


public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BaseFragment fragment = new EventParticipantsFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends BaseFragment {

        ImageView mMyImage;
        Button mButton;
        Bitmap mQRCodeImage;
        public PlaceholderFragment() {
        }

        @Override
        protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_test, container, false);
            mMyImage = (ImageView) rootView.findViewById(R.id.imageView);
            mQRCodeImage =  createQR();
            mButton = (Button) rootView.findViewById(R.id.button);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File qr_file = saveBitmap(mQRCodeImage);
                    Uri u =  Uri.fromFile(qr_file);
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("application/image");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, u);
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                }

                private File saveBitmap(Bitmap qr_code_image) {
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                        OutputStream outStream = null;
                        File file = new File(extStorageDirectory, "qr_event_image.png");
                        if (file.exists()) {
                            file.delete();
                            file = new File(extStorageDirectory, "qr_event_image.png");
                        }

                        try {
                            outStream = new FileOutputStream(file);
                            qr_code_image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                        return file;
                }
            });
            hideUtilsAndShowContentOverlay();
            return rootView;
        }

        @Override
        public String getTitle() {
            return "FragmentTest";
        }

        private Bitmap createQR(){
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("nXL6KSa0KH", getActivity());
            Bitmap bitmap = null;
            try {
                bitmap = qrCodeEncoder.encodeAsBitmap();
                mMyImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            } finally {
                return bitmap;
            }
        }
    }*/
}
