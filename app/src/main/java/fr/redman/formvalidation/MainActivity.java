package fr.redman.formvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.RoundingMode.HALF_UP;

public class MainActivity extends AppCompatActivity {

    //Init
    EditText etAmount;
    EditText etRate;
    EditText etLoanLength;
    EditText etLoanPrice;
    EditText etMonthlyPayment;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign Variable
        etAmount =findViewById(R.id.et_amount);
        etRate=findViewById(R.id.et_rate);
        etLoanLength=findViewById(R.id.et_loan_length);
        etLoanPrice=findViewById(R.id.et_loan_price);
        etMonthlyPayment=findViewById(R.id.et_monthly_payment);
        btSubmit=findViewById(R.id.bt_submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeLoanData();
//                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
//                intent.putExtra(Fields.AMOUNT.name(),Double.valueOf(etAmount.getText().toString()));
//                intent.putExtra(Fields.RATE.name(),Double.valueOf(etRate.getText().toString()));
//                intent.putExtra(Fields.LOAN_LENGTH.name(),Double.valueOf(etLoanLength.getText().toString()));
//                intent.putExtra(Fields.LOAN_PRICE.name(),Double.valueOf(etLoanPrice.getText().toString()));
//                intent.putExtra(Fields.MONTHLY_PAYMENT.name(),Double.valueOf(etMonthlyPayment.getText().toString()));
//                startActivity(intent);
            }
        });

    }

    private void computeLoanData(){
        MathContext mc = new MathContext(10, HALF_UP);
        BigDecimal amount = new BigDecimal(Double.valueOf(this.etAmount.getText().toString()),mc);
        BigDecimal oneBd = new BigDecimal(1d,mc);
        BigDecimal annualRate = new BigDecimal(Double.valueOf(this.etRate.getText().toString()),mc).divide(new BigDecimal(100), mc);

        double a = oneBd.add(annualRate,mc).doubleValue();
        double b = 1d/12d;
        double periodicIntermediate = Math.pow(a,b);
        double periodicRateDouble = periodicIntermediate - 1d;
        BigDecimal periodicRate = new BigDecimal(periodicRateDouble,mc);
        periodicRate.setScale(10, HALF_UP);

        BigDecimal lengthBd = BigDecimal.valueOf(Integer.valueOf(this.etLoanLength.getText().toString())).multiply(BigDecimal.valueOf(12));

        BigDecimal oneAndPeriodic = oneBd.add(periodicRate, mc);
        BigDecimal numerator = amount.multiply(periodicRate,mc);
        BigDecimal intermediateDenom = oneAndPeriodic.pow(-lengthBd.intValue(), mc);
        BigDecimal denominator = oneBd.subtract(intermediateDenom,mc);

        BigDecimal monthlyPayment = numerator.divide(denominator, 10, HALF_UP);

        BigDecimal loanPrice = monthlyPayment.multiply(lengthBd,mc).subtract(amount, mc);

        etMonthlyPayment.setText(monthlyPayment.toString(), TextView.BufferType.EDITABLE);
        etLoanPrice.setText(loanPrice.toString(), TextView.BufferType.EDITABLE);
    }

}