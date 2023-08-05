package com.femi.e_class.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateMatricOrIdTest {

    @Test
    fun `matric is blank fails`(){
        val result = ValidateMatricOrId().execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Matric/Id cannot be blank"
            )
        )
    }

    @Test
    fun `matric is not digits only fails`(){
        val result = ValidateMatricOrId().execute("11m")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Matric/Id must be integer numbers"
            )
        )
    }

    @Test
    fun `matric is less than 2 digits fails`(){
        val result = ValidateMatricOrId().execute("10")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Matric/Id must be more than 2 digits long"
            )
        )
    }

    @Test
    fun `acceptable matric succeed`(){
        val result = ValidateMatricOrId().execute("101")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }

}