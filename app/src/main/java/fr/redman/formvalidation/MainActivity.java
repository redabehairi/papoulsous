package fr.redman.formvalidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.RoundingMode.HALF_UP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Init
    EditText etAmount;
    EditText etRate;
    EditText etLoanLength;
    EditText etPtz;
    String loanPrice;
    String monthlyPaymentNoInsurance;
    String totalCost;
    String monthlyTotal;
    Button btSubmit;
    Button btClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign Variable
        etAmount =findViewById(R.id.et_amount);
        etRate=findViewById(R.id.et_rate);
        etLoanLength=findViewById(R.id.et_loan_length);
        etPtz =findViewById(R.id.et_ptz);
        btSubmit=findViewById(R.id.bt_submit);
        btClear=findViewById(R.id.bt_clear);
        btClear.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    private void computeLoanData(){
        MathContext mc = new MathContext(10, HALF_UP);
        BigDecimal lengthBd = BigDecimal.valueOf(Integer.valueOf(this.etLoanLength.getText().toString())).multiply(BigDecimal.valueOf(12));
        BigDecimal initialAmount = new BigDecimal(Double.valueOf(this.etAmount.getText().toString()),mc);
        BigDecimal amount;
        BigDecimal ptzAmount;
        BigDecimal monthlyPtz=null;
        if(this.etPtz.getText().length()>1){
            ptzAmount = new BigDecimal(Double.valueOf(this.etPtz.getText().toString()),mc);
            amount = initialAmount.subtract(ptzAmount);
            monthlyPtz = ptzAmount.divide(lengthBd,mc);
        }else{
            amount = initialAmount;
        }

        BigDecimal oneBd = new BigDecimal(1d,mc);
        BigDecimal annualRate = new BigDecimal(Double.valueOf(this.etRate.getText().toString()),mc).divide(new BigDecimal(100), mc);

        double a = oneBd.add(annualRate,mc).doubleValue();
        double b = 1d/12d;
        double periodicIntermediate = Math.pow(a,b);
        double periodicRateDouble = periodicIntermediate - 1d;
        BigDecimal periodicRate = new BigDecimal(periodicRateDouble,mc);
        periodicRate.setScale(10, HALF_UP);


        BigDecimal oneAndPeriodic = oneBd.add(periodicRate, mc);
        BigDecimal numerator = amount.multiply(periodicRate,mc);
        BigDecimal intermediateDenom = oneAndPeriodic.pow(-lengthBd.intValue(), mc);
        BigDecimal denominator = oneBd.subtract(intermediateDenom,mc);

        BigDecimal monthlyPayment;
        BigDecimal monthlyPaymentNoPTZ = numerator.divide(denominator, 10, HALF_UP);
        if(monthlyPtz!=null){
            monthlyPayment = monthlyPaymentNoPTZ.add(monthlyPtz,mc);
        }else{
            monthlyPayment = monthlyPaymentNoPTZ;
        }

        BigDecimal loanPrice = monthlyPaymentNoPTZ.multiply(lengthBd,mc).subtract(amount, mc);

        this.monthlyPaymentNoInsurance = monthlyPayment.setScale(0,HALF_UP).toString().concat(" €");
        this.totalCost = initialAmount.add(loanPrice,mc).setScale(0,HALF_UP).toString().concat(" €");
        this.loanPrice = loanPrice.setScale(0,HALF_UP).toString().concat(" €");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_clear:
                this.etAmount.getText().clear();
                this.etRate.getText().clear();
                this.etLoanLength.getText().clear();
                this.etPtz.getText().clear();
                break;
            case R.id.bt_submit:
                executeComputingAndDisplayResult();
                break;
            default:
                break;
        }
    }

    private void executeComputingAndDisplayResult(){
        computeLoanData();
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra(Fields.MONTHLY_NO_INSURANCE.name(), monthlyPaymentNoInsurance);
        intent.putExtra(Fields.LOAN_PRICE.name(),loanPrice);
        intent.putExtra(Fields.TOTAL_COST.name(),totalCost);
        startActivity(intent);
    }
}