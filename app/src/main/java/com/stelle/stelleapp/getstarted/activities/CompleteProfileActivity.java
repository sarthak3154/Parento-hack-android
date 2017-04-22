package com.stelle.stelleapp.getstarted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.stelle.stelleapp.BaseActivity;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.getstarted.interfaces.CompleteProfileContract;
import com.stelle.stelleapp.getstarted.parsers.CompleteProfileRequestParser;
import com.stelle.stelleapp.getstarted.presenters.CompleteProfilePresenter;
import com.stelle.stelleapp.homescreen.activities.HomeScreenActivity;
import com.stelle.stelleapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 22-04-2017
 */

public class CompleteProfileActivity extends BaseActivity implements CompleteProfileContract.View, AdapterView.OnItemSelectedListener {

    @Bind(R.id.childName)
    EditText childName;
    @Bind(R.id.classSection)
    EditText classSection;
    @Bind(R.id.classStd)
    EditText classStd;
    @Bind(R.id.subjectName)
    EditText subjectName;
    @Bind(R.id.btn_done)
    AppCompatButton btnDone;
    @Bind(R.id.select_category)
    Spinner typeCategory;
    @Bind(R.id.selectSchool)
    Spinner schoolNameSpinner;

    private String schoolName;
    private String userType;
    List<String> schoolNames;

    private CompleteProfileContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        init();

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        presenter = new CompleteProfilePresenter(this);
        getApiComponent().inject((CompleteProfilePresenter) presenter);
        typeCategory.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Parent");
        categories.add("Teacher");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        typeCategory.setAdapter(dataAdapter);

        schoolNames = new ArrayList<>();
        schoolNames.add("DL DAV Model School, Pitampura");
        schoolNames.add("Delhi Public School, RK Puram");
        schoolNames.add("Sachdeva Public School, Pitampura");
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schoolNames);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolNameSpinner.setAdapter(schoolAdapter);
    }

    @Override
    public void moveToNextScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(CompleteProfileActivity.this, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.select_category) {
            switch (i) {
                case 0:
                    userType = "Parent";
                    break;
                case 1:
                    userType = "Teacher";
                    break;
            }
        } else if (spinner.getId() == R.id.selectSchool) {
            switch (i) {
                case 0:
                    schoolName = schoolNames.get(0);
                    break;
                case 1:
                    schoolName = schoolNames.get(1);
                    break;
                case 2:
                    schoolName = schoolNames.get(2);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick(R.id.btn_done)
    public void onClickCompleteProfileApi() {
        if (childName.getText().toString().isEmpty() || subjectName.getText().toString().isEmpty() || classSection.getText().toString().isEmpty() || classStd.getText().toString().isEmpty()) {
            Utils.showToast(this, getString(R.string.string_enter_details));
        } else {
            CompleteProfileRequestParser requestParser = new CompleteProfileRequestParser();
            requestParser.setSchoolName(schoolName);
            requestParser.setUserType(userType);
            requestParser.setSection(classSection.getText().toString());
            requestParser.setStudentName(childName.getText().toString());
            requestParser.setStandard(classStd.getText().toString());
            if (userType.equals("Teacher")) {
                requestParser.setSubject(subjectName.getText().toString());
            } else {
                requestParser.setSubject("");
            }
            presenter.callUpdateProfileApi(requestParser);
        }
    }
}
