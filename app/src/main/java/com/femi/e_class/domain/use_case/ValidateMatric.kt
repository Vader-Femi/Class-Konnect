package com.femi.e_class.domain.use_case

import android.app.Application
import androidx.core.text.isDigitsOnly

class ValidateMatric(val appContext: Application) {

    fun execute(matric: String): ValidationResult{
        if (!matric.isDigitsOnly()){
            return ValidationResult(
                false,
                "Matric/Id must be integer numbers"
            )
        }
        if (matric.length <= 2){
            return ValidationResult(
                false,
                "Matric/Id must be more than 2 digits long"
            )
        }
        return ValidationResult(
            true
        )
    }
}