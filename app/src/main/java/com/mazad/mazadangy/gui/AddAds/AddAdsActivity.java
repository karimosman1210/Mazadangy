package com.mazad.mazadangy.gui.AddAds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mazad.mazadangy.R;
import com.mazad.mazadangy.adapter.UploadListAdapter;
import com.mazad.mazadangy.gui.category.CategoryActivity;
import com.mazad.mazadangy.utels.HelperMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddAdsActivity extends AppCompatActivity {


    CheckBox checkBox_money;
    LinearLayout layout_money, layoutDate;
    ImageView addBtn, minBtn;
    TextView tvAddMoney, tvStartDate, tvEndDate;
    Button addPost;
    private UploadListAdapter uploadListAdapter;
    DatabaseReference mDatabase;
    DatabaseReference mRef;
    FirebaseUser currentFirebaseUser;
    private RecyclerView mUploadList;
    private static final int RUSLET_LOAD_IMAGE = 1;
    static Uri image_item;
    String uri_load;
    private StorageReference mStorageReference;
    StorageReference filepath;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    RadioGroup rgBackSale, rgAdsLength, rgAdsTime;
    EditText etPandNum, etDesc, etStartPrice;
    ImageView adsImage;
    private static final int RESULT_LOAD_IMAGE = 1;
    public static int addmoneyNumber;
    private StorageReference mStorage;
    List<Uri> uriList;
    public String back_sale = "0", count_price, desc_money, end_ads = "0", day_num = "0", start_ads, end_time = "0", pand_num, start_price, status_money, stop_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);
        uriList = new ArrayList<>();
        intilizeData();
        rgChoise();
        setDate();
        mUploadList = (RecyclerView) findViewById(R.id.mUploadList);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.child("mony_post").push();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser == null) {
            Toast.makeText(this, "برجاء تسجيل الدخول للتمكن من نشر اعلانات", Toast.LENGTH_SHORT).show();

        } else {
            addPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pand_num = etPandNum.getText().toString();
                    desc_money = etDesc.getText().toString();
                    start_price = etStartPrice.getText().toString();
                    HelperMethods.showDialog(AddAdsActivity.this, "من فضلك انتظر", "جاري التحميل ....");

                    if (!(etPandNum.getText().toString().isEmpty() || etDesc.getText().toString().isEmpty() || etStartPrice.getText().toString().isEmpty())) {
                        if (!(back_sale.equals("0") || day_num.equals("0") || end_time.equals("0") ||uriList.isEmpty() || end_ads.equals("0"))) {
                            //     Toast.makeText(AddPostActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                            for (int x = 0; x < uriList.size(); x++) {
                                final int s = x;
                                Log.d("dasdasdasd", uriList.get(s).toString());
                                StorageReference filepath = mStorage.child("Photos").child(uriList.get(x).getLastPathSegment());
                                filepath.putFile(uriList.get(s)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();

                                                    mRef.child("imge").child(String.valueOf(s)).setValue(imageUrl);
                                                }
                                            });
                                        }

//                        startActivity(new Intent(User_Profile_image.this , HomeScreen.class));

                                    }
                                });


                                    mRef.child("back_sale").setValue(back_sale);
                                    mRef.child("count_price").setValue(addmoneyNumber + "");
                                    mRef.child("desc_money").setValue(desc_money);
                                    mRef.child("end_ads").setValue(end_ads);
                                    mRef.child("end_time").setValue(end_time);
                                    mRef.child("pand_num").setValue(pand_num);
                                    mRef.child("start_price").setValue(start_price);
                                    mRef.child("end_price").setValue(start_price);
                                    mRef.child("status_money").setValue(status_money);
                                    mRef.child("stop_ad").setValue("false");
//
                                    mRef.child("id_post").setValue(mRef.getKey().toString());

                                    mRef.child("userId").setValue(currentFirebaseUser.getUid().toString());
                                    //mRef.child("start_ad").setValue("false");
                                    //mRef.child("day_num").setValue(day_num);

                                    //Toast.makeText(AddPostActivity.this, pand_num + "" + desc_money + "" + start_price + "" + back_sale + "" + day_num + "" + start_ads + "" + end_ads + "" + end_time + "" + status_money, Toast.LENGTH_LONG).show();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        HelperMethods.hideDialog(AddAdsActivity.this);
                                        Intent intent = new Intent(AddAdsActivity.this, CategoryActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(AddAdsActivity.this,"م اضافه الاعلان بنجاح",Toast.LENGTH_SHORT).show();
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("result",55);
                                        setResult(Activity.RESULT_OK,returnIntent);
                                        finish();



                                    }
                                }, 8000);


                            }


                        } else {
                            HelperMethods.hideDialog(AddAdsActivity.this);
                            Toast.makeText(AddAdsActivity.this, "برجاء اختيار جميع العناصر", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        HelperMethods.hideDialog(AddAdsActivity.this);
                        Toast.makeText(AddAdsActivity.this, "برجاء ادخال جميع البيانات", Toast.LENGTH_SHORT).show();


                    }
                }

            });
        }

        adsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, RUSLET_LOAD_IMAGE);

            }
        });


    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedimage = data.getData();
                uriList.add(selectedimage);

                uploadListAdapter = new UploadListAdapter(uriList);
                mUploadList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                mUploadList.setHasFixedSize(true);
                mUploadList.setAdapter(uploadListAdapter);
            }

        }
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void intilizeData() {
        checkBox_money = findViewById(R.id.checkBoxMoneyAddAdsActivity);
        layout_money = findViewById(R.id.layoutAddMoneyAddAdsActivity);
        addBtn = findViewById(R.id.buttonAddMoneyAddAdsActivity);
        minBtn = findViewById(R.id.buttonRemoveMoneyAddAdsActivity);
        tvAddMoney = findViewById(R.id.TvMoneyAddAdsActivity);
        tvStartDate = findViewById(R.id.tvStartDateAddAdsActivity);
        tvEndDate = findViewById(R.id.tvEndDateAddAdsActivity);
        rgBackSale = findViewById(R.id.rgBackSaleAddAdsActivity);
        rgAdsLength = findViewById(R.id.rgAdsLengthAddAdsActivity);
        rgAdsTime = findViewById(R.id.rgAdsTimeAddAdsActivity);
        etPandNum = findViewById(R.id.etPandNumAddAdsActivity);
        etDesc = findViewById(R.id.etDescAddAdsActivity);
        etStartPrice = findViewById(R.id.etStartPriceAddAdsActivity);
        adsImage = findViewById(R.id.imageAddAdsActivity);
        addPost = findViewById(R.id.addBtnAddAdsActivity);
        layoutDate = findViewById(R.id.layoutDateAddAdsActivity);
    }

    void addMoney() {
        addmoneyNumber = 50;


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmoneyNumber += 50;
                tvAddMoney.setText(addmoneyNumber + "");
            }
        });
        minBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addmoneyNumber > 50) {
                    addmoneyNumber -= 50;
                    tvAddMoney.setText(addmoneyNumber + "");
                } else {
                    Toast.makeText(AddAdsActivity.this, "can,t decrease less 50 ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        count_price = (addmoneyNumber + "");


    }

    public void setDate() {


        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AddAdsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + (year));

                        start_ads = (dayOfMonth + "/" + (month + 1) + "/" + (year) + " 24:00:00");

                    }
                }, day, month, year);
                datePickerDialog.show();
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(AddAdsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvEndDate.setText(dayOfMonth + "/" + (month + 1) + "/" + (year));
                        end_ads = (dayOfMonth + "/" + (month + 1) + "/" + (year) + " 24:00:00");
                    }
                }, day, month, year);

                //   String format1 = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());

                datePickerDialog.show();
            }
        });

    }

    public void rgChoise() {
        checkBox_money.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("checked ");
                    layout_money.setVisibility(View.VISIBLE);
                    addMoney();
                } else {
                    layout_money.setVisibility(View.GONE);
                    addmoneyNumber = 0;
                    tvAddMoney.setText(50 + "");
                    System.out.println("not cheacked " + addmoneyNumber);
                }

            }
        });

        if (!checkBox_money.isChecked()) {
            addmoneyNumber = 0;
        }

        rgBackSale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbYesBackSaleActivity:
//                        Toast.makeText(AddAdsActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        // do operations specific to this selection
                        back_sale = "رجوع";
                        break;
                    case R.id.rbNoBackSaleActivity:
                        // do operations specific to this selection
//                        Toast.makeText(AddAdsActivity.this, "No", Toast.LENGTH_SHORT).show();
                        back_sale = "بدون رجوع";

                        break;

                }
            }
        });
        rgAdsLength.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbOneDayAdsLengthAddAdsActivity:
                        day_num = "يوم واحد";
                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        start_ads = (day + "/" + (month + 1) + "/" + (year) + " 24:00:00");
                        end_ads = (day + "/" + (month + 1) + "/" + (year) + " 24:00:00");
                        layoutDate.setVisibility(View.GONE);
//                        Toast.makeText(AddAdsActivity.this, "One Day", Toast.LENGTH_SHORT).show();
                        // do operations specific to this selection
                        break;
                    case R.id.rbMoreDaysAdsLengthAddAdsActivity:
                        // do operations specific to this selection
//                        Toast.makeText(AddAdsActivity.this, "MoreDays", Toast.LENGTH_SHORT).show();
                        layoutDate.setVisibility(View.VISIBLE);
                        day_num = "عدة ايام";

                        break;

                }
            }
        });
        rgAdsTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbTenAdsTimeAddAdsActivity:
//                        Toast.makeText(AddAdsActivity.this, "10:00", Toast.LENGTH_SHORT).show();
                        // do operations specific to this selection
                        end_time = "العاشرة مساء";
                        break;
                    case R.id.rbTwelveAdsTimeAddAdsActivity:
                        // do operations specific to this selection
//                        Toast.makeText(AddAdsActivity.this, "12:00", Toast.LENGTH_SHORT).show();
                        end_time = "منتصف الليل";
                        break;

                }
            }
        });

        Spinner spinner = findViewById(R.id.spinnerStateAddAdsActivity);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("JAVA");
        arrayList.add("ANDROID");
        arrayList.add("C Language");
        arrayList.add("CPP Language");
        arrayList.add("Go Language");
        arrayList.add("AVN SYSTEMS");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
                status_money = tutorialsName;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


}
