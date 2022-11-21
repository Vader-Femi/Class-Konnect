package com.femi.e_class.domain.use_case

import androidx.core.text.isDigitsOnly

class ValidateMatric {

    fun execute(matric: String): ValidationResult{
        if (!matric.isDigitsOnly()){
            return ValidationResult(
                false,
                "Matric must be a integer numbers"
            )
        }
        if (matric.length != 9){
            return ValidationResult(
                false,
                "Matric must be 9 digits"
            )
        }
        return ValidationResult(
            true
        )
    }
}