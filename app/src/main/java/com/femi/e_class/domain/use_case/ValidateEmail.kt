package com.femi.e_class.domain.use_case

//import android.util.Patterns
import androidx.core.util.PatternsCompat

class ValidateEmail() {

    fun execute(email: String): ValidationResult{
        if (email.isBlank()){
            return ValidationResult(
                false,
                "Email can't be black"
            )
        }
        if (email.any { it.isUpperCase() }){
            return ValidationResult(
                false,
                "Email must not contain upper-case letters"
            )
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                false,
                "That's not a valid email"
            )
        }
        return ValidationResult(
            true
        )
    }
}