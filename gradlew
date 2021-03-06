/*Activity that allows users input to add grades for the gpa calculation. */

package com.utilityapp.gpa_wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddGradesActivity extends AppCompatActivity {
    static int ADD_GRADES_REQUEST = 1;
    EditText textGradeOne;
    EditText textGradeTwo;
    EditText textGradeThree;
    EditText textGradeFour;
    EditText errorText;
    Button save;
    Calculator calculator = new Calculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grades);

        save = findViewById(R.id.saveButton);

        errorText = findViewById(R.id.errorText);

        textGradeOne = findViewById(R.id.textGradeOne);
        textGradeTwo = findViewById(R.id.textGradeTwo);
        textGradeThree = findViewById(R.id.textGradeThree);
        textGradeFour = findViewById(R.id.textGradeFour);

    }

    public void gradeClicked(View view){
        if(textGradeOne.getText() == null){
            errorText.setText(R.string.error_grades);
            
        }
    }

    // Action preformed on user press save button, sending total grade to the main activity.
    public void saveClicked(View view) {
        int grade1 = Integer.parseInt(textGradeOne.getText().toString());
        int grade2 = Integer.parseInt(textGradeTwo.getText().toString());
        int grade3 = Integer.parseInt(textGradeThree.getText().toString());
        int grade4 = Integer.parseInt(textGradeFour.getText().toString());
        //stores results in array form for possible future scalability.
        int[] grades = new int[]{grade1, grade2, grade3, grade4};
        //User input error checking.
        boolean invalidGrade = false;
        for (int grade: grades
             ) {
            if (grade <= 0 || grade > 7){
                invalidGrade = true;
                save.setText(R.string.error_grades);
        }
        if (invalidGrade){
            textGradeOne.setText(R.string.defaultGrade_0);
            textGradeTwo.setText(R.string.defaultGrade_0);
            textGradeThree.setText(R.string.defaultGrade_0);
            textGradeFour.setText(R.string.defaultGrade_0);
        } else {
            //Sends error checked results ot main activity.
            save.setText(R.string.save);
            double totalGrade = calculator.totalGrade(grades);

            Intent intent = new Intent();
            intent.putExtra("totalGrade", totalGrade);
            setResult(RESULT_OK, intent);
            finish();
        }

    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       