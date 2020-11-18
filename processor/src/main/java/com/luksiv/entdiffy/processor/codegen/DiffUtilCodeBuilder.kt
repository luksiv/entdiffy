package com.luksiv.entdiffy.processor.codegen

import com.luksiv.entdiffy.processor.models.ModelData
import com.squareup.kotlinpoet.ClassName

class DiffUtilCodeBuilder(
    private val data: ModelData,
) {

    private val diffResultName = "${data.modelName}DiffResult"
    private val diffResultClassName = ClassName(data.packageName, diffResultName)

    private val diffUtilName = "${data.modelName}DiffUtil"
    private val diffUtilClassName = ClassName(data.packageName, diffUtilName)

//    fun build(): TypeSpec {
//        return TypeSpec.classBuilder()
//    }
}

//// GENERATED
//data class PersonDiffResult(
//    val firstNameChanged: Boolean,
//    val lastNameChanged: Boolean,
//    val addressChanged: Boolean,
//    val countryChanged: Boolean,
//    val ageChanged: Boolean,
//)
//
//// GENERATED
//object PersonDiffUtil {
//
//    fun getDiff(a: Person?, b: Person?): PersonDiffResult {
//        return PersonDiffResult(
//            a?.firstName != b?.firstName,
//            a?.lastName != b?.firstName,
//            a?.address != b?.address,
//            a?.country != b?.country,
//            a?.age != b?.age,
//        )
//    }
//}