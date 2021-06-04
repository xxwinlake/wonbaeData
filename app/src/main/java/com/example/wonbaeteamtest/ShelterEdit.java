package com.example.wonbaeteamtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;


public class ShelterEdit extends AppCompatActivity {
    private EditText nameText;
    private EditText addressText;
    private EditText providerText;
    private ArrayList<String> Subject = new ArrayList<>();
    private Spinner mSpinner;
    private int subjectPosition;
    private final int GET_GALLERY_IMAGE = 200;
    private final int GET_GALLERY_VIDEO = 300;

    private ImageView imageView;
    private byte[] byteArray;
    Bitmap img;
    private int id;
    private File mFile;
    private Button mBtnRecord;
    private MediaRecorder mRecord;
    private boolean mIsRecording;
    private Button mBtnPlay;
    private MediaPlayer mPlayer;
    private Uri audio;

    private VideoView mVideoView;
    private Uri video;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_edit);


        imageView = (findViewById(R.id.shtImg));
        nameText = (EditText) findViewById(R.id.shtName);
        addressText = (EditText) findViewById(R.id.shtAdd);
        providerText = (EditText) findViewById(R.id.shtWho);
        mBtnRecord = (Button) findViewById(R.id.recordbt);
        mBtnPlay = (Button) findViewById(R.id.playbt);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        byteArray = intent.getByteArrayExtra("image");
        if (byteArray != null) {
            Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(image);
        } else {
            imageView.setImageResource(R.drawable.defaultimg);
        }
        nameText.setText(intent.getStringExtra("name"));
        addressText.setText(intent.getStringExtra("address"));
        providerText.setText(intent.getStringExtra("provider"));
        if (intent.getStringExtra("audio") == null) {
        } else {
            audio = Uri.parse(intent.getStringExtra("audio"));
        }
        if (intent.getStringExtra("video") == null) {
        } else {
            video = Uri.parse(intent.getStringExtra("video"));
        }
        Subject.add("지진");
        Subject.add("해일");
        Subject.add("화산");

        mSpinner = (Spinner) findViewById(R.id.editSubject);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Subject);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter1);

        //subjectPosition에 subject 값을 인텐트로 가져옴.
        subjectPosition = intent.getIntExtra("subject", -1);
        mSpinner.setSelection(subjectPosition);//초기값

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecord != null) {
            mRecord.release();
            mRecord = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    imageView.setImageBitmap(img);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                    in.close();
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GET_GALLERY_VIDEO) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                mVideoView = (VideoView) findViewById(R.id.video_view);

                MediaController mc = new MediaController(this);
                mVideoView.setMediaController(mc);
                mVideoView.setVideoPath(String.valueOf(video));
                mVideoView.setOnPreparedListener(mp -> {
                    mp.setLooping(false);
                    mp.seekTo(0);
                    Toast.makeText(this, "동영상 재생준비완료", Toast.LENGTH_SHORT).show();
                    mp.start();
                });
            } else {
                Toast.makeText(this, "동영상 선택 취소", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }


    public void mOnClick(View view) {
        Intent data = new Intent();

        switch (view.getId()) {
            case R.id.btnSave:
                //저장버튼을 눌렀을때, 문자열 값이 없으면 대피소이름을 입력하라고 toast출력.
                if (nameText.getText().toString().length() <= 0) {
                    Toast.makeText(this, "대피소 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else {

                    data.putExtra("id", id);
                    if (video == null) {
                        String nullvalues = "";
                        video = Uri.parse(nullvalues);
                    }
                    if (audio == null) {
                        String nullvalues = "";
                        audio = Uri.parse(nullvalues);
                    }
                    MainActivity.putExtraInfo(data, id, byteArray, subjectPosition, nameText.getText().toString(),
                            addressText.getText().toString(), providerText.getText().toString(), audio.toString(), video.toString());
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                }
            case R.id.btnCancel:
                /*Intent intent = new Intent(ShelterEdit.this, MainActivity.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
                startActivity(intent);  //인텐트 이동
                finish();*/
                onBackPressed();
                break;
            case R.id.recordbt:
                if (!mIsRecording) {
                    checkDangerousPermissions();
                    File sdcard = Environment.getExternalStorageDirectory();
                    mFile = new File(sdcard.getAbsolutePath() + "/record.3gp");
                    if (mRecord == null) {
                        mRecord = new MediaRecorder();
                    } else {
                        mRecord.reset();
                    }
                    try {
                        mRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                        mRecord.setOutputFile(mFile.getAbsolutePath());
                        Toast.makeText(this, "녹음을 시작하세요", Toast.LENGTH_SHORT).show();
                        mRecord.prepare();
                        mRecord.start();
                        mIsRecording = true;
                        mBtnRecord.setText("녹음 중지");
                        mBtnPlay.setEnabled(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mRecord.stop();
                    mIsRecording = false;
                    mBtnRecord.setText("녹음 시작");
                    audio = Uri.fromFile(mFile);
                    mBtnPlay.setEnabled(true);
                }
                break;
            case R.id.playbt:
                if (!mFile.exists()) {
                    Toast.makeText(this, "녹음 파일 없음!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPlayer == null) {
                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mBtnRecord.setEnabled(true);
                            mBtnPlay.setEnabled(true);
                        }
                    });
                } else {
                    mPlayer.reset();
                }
                try {
                    mPlayer.setDataSource(getApplicationContext(), audio);
                    mPlayer.prepare();
                    mPlayer.start();
                    mBtnRecord.setEnabled(false);
                    mBtnPlay.setEnabled(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.video_bt:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
                startActivityForResult(intent, GET_GALLERY_VIDEO);
                break;
        }
    }

    public void onBackPressed() {
        //super.onBackPressed();
        Intent toMain = new Intent();
        setResult(5, toMain);
        finish();
    }
}

