package fr.redman.formvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvLoanPrice;
    TextView tvMonthlyPayment;
    TextView tvLoanInsurance;
    TextView tvMonthlyInsurance;
    TextView tvNotaryFees;
    TextView tvTotal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        //Assign varialbes
        tvLoanPrice=findViewById(R.id.tv_loan_price);
        tvMonthlyPayment=findViewById(R.id.tv_monthly_payment_no_insurance);
        tvLoanInsurance =findViewById(R.id.tv_loan_insurance);
        tvMonthlyInsurance=findViewById(R.id.tv_monthly_insurance);
        tvNotaryFees=findViewById(R.id.tv_notary_fees);
        tvTotal=findViewById(R.id.tv_total);

        tvLoanPrice.setText(intent.getStringExtra(Fields.LOAN_PRICE.name()));
        tvMonthlyPayment.setText(intent.getStringExtra(Fields.MONTHLY_NO_INSURANCE.name()));
        tvTotal.setText(intent.getStringExtra(Fields.TOTAL_COST.name()));


    }
}
