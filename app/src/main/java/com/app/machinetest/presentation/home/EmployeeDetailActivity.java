package com.app.machinetest.presentation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.machinetest.R;
import com.app.machinetest.Utilities.ConstantMethods;
import com.app.machinetest.models.EmployeeListModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeDetailActivity extends AppCompatActivity {
    ShimmerFrameLayout shimmerFrameLayout;
    TextView tvName, tvPhone, tvEmail, tvAddress, tvWebsite, tvCompany;
    ImageView ivProfile,ivBack;
    LinearLayout llMain;

    Context context;
    EmployeeListModel employeeListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        initViews();
    }

    private void initViews() {

        context = EmployeeDetailActivity.this;
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvName = findViewById(R.id.tvEmpName);
        ivProfile = findViewById(R.id.iv_profile);
        tvCompany = findViewById(R.id.tvCompany);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvAddress = findViewById(R.id.tvAddress);
        ivBack = findViewById(R.id.ivBack);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        llMain = findViewById(R.id.llMain);


        if (getIntent() != null & getIntent().hasExtra("EmployeeItem")) {
            employeeListModel = (EmployeeListModel) getIntent().getSerializableExtra("EmployeeItem");

            setValues();

        }

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setValues() {

        ConstantMethods.loadShimmerImage(context, employeeListModel.getProfileImage(),
                ivProfile, shimmerFrameLayout);
        tvPhone.setText(employeeListModel.getPhone() != null ? "Phone : " + employeeListModel.getPhone() : "--- ------ ------");
        tvEmail.setText(employeeListModel.getEmail() != null ? "Email : " + employeeListModel.getEmail() : "--- ------ ------");
        tvName.setText(employeeListModel.getName() != null ? employeeListModel.getName() : "--- ------ ------");

        tvWebsite.setText(employeeListModel.getWebsite() != null ? "Website : " +
                employeeListModel.getWebsite() : "--- ------ ------");

        String company = "";
        if (employeeListModel.getCompany() != null) {

            if (employeeListModel.getCompany().getName() != null) {
                company = employeeListModel.getCompany().getName();

                if (employeeListModel.getCompany().getCatchPhrase() != null) {
                    company = company + " , " + employeeListModel.getCompany().getCatchPhrase();
                    if (employeeListModel.getCompany().getBs() != null) {
                        company = company + " , " + employeeListModel.getCompany().getBs();
                    }
                } else {
                    if (employeeListModel.getCompany().getBs() != null) {
                        company = company + " , " + employeeListModel.getCompany().getBs();
                    }
                }

            }
        }
        tvCompany.setText(company);
        String address = "";
        if (employeeListModel.getAddress() != null) {

            if (employeeListModel.getAddress().getCity() != null) {
                address = employeeListModel.getAddress().getCity();

                if (employeeListModel.getAddress().getStreet() != null) {
                    address = address + " , " + employeeListModel.getAddress().getStreet();
                    if (employeeListModel.getAddress().getZipcode() != null) {
                        address = address + " , " + employeeListModel.getAddress().getZipcode();
                    }
                } else {
                    if (employeeListModel.getAddress().getZipcode() != null) {
                        address = address + " , " + employeeListModel.getAddress().getZipcode();
                    }
                }

            }
        }
        tvAddress.setText(address);


        tvWebsite.setText(employeeListModel.getWebsite() != null ? "Website : " + employeeListModel.getWebsite() : "--- ------ ------");


    }
}