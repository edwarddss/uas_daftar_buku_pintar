package com.example.Daftar_Buku_Pintar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.list_buku_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {

    private Spinner mPilihtema;
    private EditText mJudulbuku, mNamapengarang, mPenerbit, mTahunterbit;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;

    Calendar myCalendar = Calendar.getInstance();

    private int mtema = 0;
    public static final int TANPA_TEMA = 0;
    public static final int KAMUS = 1;
    public static final int ILMU_PENGETAHUAN = 2;

    private String Judulbuku, Namapengarang, Penerbit, Tahunterbit, picture;
    private int id, Pilihtema;

    private Menu action;
    private Bitmap bitmap;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_buku);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mJudulbuku = findViewById(R.id.judulbuku);
        mNamapengarang = findViewById(R.id.namapengarang);
        mPenerbit = findViewById(R.id.penerbit);
        mTahunterbit = findViewById(R.id.tahunterbit);
        mPicture = findViewById(R.id.picture);
        mFabChoosePic = findViewById(R.id.fabChoosePic);

        mPilihtema = findViewById(R.id.pilihtema);
        mTahunterbit = findViewById(R.id.tahunterbit);

        mTahunterbit.setFocusableInTouchMode(false);
        mTahunterbit.setFocusable(false);
        mTahunterbit.setOnClickListener(v -> new DatePickerDialog(EditorActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        setupSpinner();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Judulbuku = intent.getStringExtra("judulbuku");
        Namapengarang = intent.getStringExtra("namapengarang");
        Penerbit = intent.getStringExtra("penerbit");
        Tahunterbit = intent.getStringExtra("tahunterbit");
        picture = intent.getStringExtra("picture");
        Pilihtema = intent.getIntExtra("pilihtema", 0);

        setDataFromIntentExtra();

    }

    private void setDataFromIntentExtra() {

        if (id != 0) {

            readMode();
            getSupportActionBar().setTitle("Edit " + Judulbuku.toString());

            mJudulbuku.setText(Judulbuku);
            mNamapengarang.setText(Namapengarang);
            mPenerbit.setText(Penerbit);
            mTahunterbit.setText(Tahunterbit);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.ic_baseline_book_24);
            requestOptions.error(R.drawable.ic_baseline_book_24);

            Glide.with(EditorActivity.this)
                    .load(picture)
                    .apply(requestOptions)
                    .into(mPicture);

            switch (Pilihtema) {
                case KAMUS:
                    mPilihtema.setSelection(1);
                    break;
                case ILMU_PENGETAHUAN:
                    mPilihtema.setSelection(2);
                    break;
                default:
                    mPilihtema.setSelection(0);
                    break;
            }

        } else {
            getSupportActionBar().setTitle("Tambah Judul Baru");
        }
    }

    private void setupSpinner(){
        ArrayAdapter PilihtemaSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_tema_options, android.R.layout.simple_spinner_item);
        PilihtemaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mPilihtema.setAdapter(PilihtemaSpinnerAdapter);

        mPilihtema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.kamus))) {
                        mtema = KAMUS;
                    } else if (selection.equals(getString(R.string.ilmu_pengetahuan))) {
                        mtema = ILMU_PENGETAHUAN;
                    } else {
                        mtema = TANPA_TEMA;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mtema = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buku, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        if (id == 0){

            action.findItem(R.id.menu_edit).setVisible(false);
            action.findItem(R.id.menu_delete).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
            case R.id.menu_edit:
                //Edit

                editMode();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mJudulbuku, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_delete).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;
            case R.id.menu_save:
                //Save

                if (id == 0) {

                    if (TextUtils.isEmpty(mJudulbuku.getText().toString()) ||
                            TextUtils.isEmpty(mNamapengarang.getText().toString()) ||
                            TextUtils.isEmpty(mPenerbit.getText().toString()) ||
                            TextUtils.isEmpty(mTahunterbit.getText().toString()) ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Form harus terisi lengkap!");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

                    else {

                        postData("isi");
                        action.findItem(R.id.menu_edit).setVisible(true);
                        action.findItem(R.id.menu_save).setVisible(false);
                        action.findItem(R.id.menu_delete).setVisible(true);

                        readMode();

                    }

                } else {

                    updateData("update", id);
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                    action.findItem(R.id.menu_delete).setVisible(true);

                    readMode();
                }

                return true;
            case R.id.menu_delete:

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditorActivity.this);
                dialog.setMessage("Hapus buku ini ?");
                dialog.setPositiveButton("Ya" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData("hapus", id, picture);
                    }
                });
                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setTahunterbit();
        }

    };

    private void setTahunterbit() {
        String myFormat = "dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTahunterbit.setText(sdf.format(myCalendar.getTime()));
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mPicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        readMode();

        String Judulbuku = mJudulbuku.getText().toString().trim();
        String Namapengarang = mNamapengarang.getText().toString().trim();
        String Penerbit = mPenerbit.getText().toString().trim();
        int Pilihtema = mtema;
        String Tahunterbit = mTahunterbit.getText().toString().trim();
        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Buku> call = apiInterface.insertBuku(key, Judulbuku, Namapengarang, Penerbit, Pilihtema, Tahunterbit, picture);

        call.enqueue(new Callback<Buku>() {
            @Override
            public void onResponse(Call<Buku> call, Response<Buku> response) {

                progressDialog.dismiss();

                Log.i(EditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Buku> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateData(final String key, final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Update...");
        progressDialog.show();

        readMode();

        String Judulbuku = mJudulbuku.getText().toString().trim();
        String Namapengarang = mNamapengarang.getText().toString().trim();
        String Penerbit = mPenerbit.getText().toString().trim();
        int Pilihtema = mtema;
        String Tahunterbit = mTahunterbit.getText().toString().trim();
        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Buku> call = apiInterface.updateBuku(key, id, Judulbuku, Namapengarang, Penerbit, Pilihtema, Tahunterbit, picture);

        call.enqueue(new Callback<Buku>() {
            @Override
            public void onResponse(Call<Buku> call, Response<Buku> response) {

                progressDialog.dismiss();

                Log.i(EditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Buku> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData(final String key, final int id, final String pic) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Hapus...");
        progressDialog.show();

        readMode();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Buku> call = apiInterface.deleteBuku(key, id, pic);

        call.enqueue(new Callback<Buku>() {
            @Override
            public void onResponse(Call<Buku> call, Response<Buku> response) {

                progressDialog.dismiss();

                Log.i(EditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Buku> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("RestrictedApi")
    void readMode(){

        mJudulbuku.setFocusableInTouchMode(false);
        mNamapengarang.setFocusableInTouchMode(false);
        mPenerbit.setFocusableInTouchMode(false);
        mJudulbuku.setFocusable(false);
        mNamapengarang.setFocusable(false);
        mPenerbit.setFocusable(false);

        mPilihtema.setEnabled(false);
        mTahunterbit.setEnabled(false);

        mFabChoosePic.setVisibility(View.INVISIBLE);

    }

    @SuppressLint("RestrictedApi")
    private void editMode(){

        mJudulbuku.setFocusableInTouchMode(true);
        mNamapengarang.setFocusableInTouchMode(true);
        mPenerbit.setFocusableInTouchMode(true);

        mPilihtema.setEnabled(true);
        mTahunterbit.setEnabled(true);

        mFabChoosePic.setVisibility(View.VISIBLE);
    }
}
