package com.femi.e_class.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)