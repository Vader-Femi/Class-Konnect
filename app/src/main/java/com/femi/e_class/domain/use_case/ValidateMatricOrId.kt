package com.femi.e_class.domain.use_case

import android.content.Context
import com.femi.e_class.R

class ValidateMatricOrId(val context: Context) {

    fun execute(matricOrId: String): ValidationResult{
        if (matricOrId.isBlank()){
            return ValidationResult(
                false,
                context.getString(R.string.matric_or_id_black_error)
            )
        }
        matricOrId.forEach {
            if (!it.isDigit()){
                return ValidationResult(
                    false,
                    context.getString(R.string.matric_or_id_not_int_error)
                )
            }
        }
        if (matricOrId.length <= 2){
            return ValidationResult(
                false,
                context.getString(R.string.matric_id_too_short_error)
            )
        }
        return ValidationResult(
            true
        )
    }
}