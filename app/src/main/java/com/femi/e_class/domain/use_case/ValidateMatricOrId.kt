package com.femi.e_class.domain.use_case

class ValidateMatricOrId {

    fun execute(matricOrId: String): ValidationResult{
        if (matricOrId.isBlank()){
            return ValidationResult(
                false,
                "Matric/Id cannot be blank"
            )
        }
        matricOrId.forEach {
            if (!it.isDigit()){
                return ValidationResult(
                    false,
                    "Matric/Id must be integer numbers"
                )
            }
        }
        if (matricOrId.length <= 2){
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