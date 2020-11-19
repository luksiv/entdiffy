package com.luksiv.entdiffy.processor.codegen

import com.luksiv.entdiffy.processor.models.ModelData
import com.squareup.kotlinpoet.*

class DiffResultCodeBuilder(
    private val data: ModelData,
) {

    private val diffResultName = "${data.modelName}DiffResult"
    private val diffResultClassName = ClassName(data.packageName, diffResultName)

    fun build(): TypeSpec {
        val primaryConstructorBuilder = FunSpec.constructorBuilder()
        val propertySpecs = mutableListOf<PropertySpec>()

        data.modelFields.forEach {
            val fieldName = getConstructorFieldName(it.fieldName)
            val fieldType = Boolean::class.java

            primaryConstructorBuilder.addParameter(fieldName, fieldType)

            val propertySpec = PropertySpec.builder(fieldName, fieldType)
                .initializer(fieldName)

            propertySpecs.add(propertySpec.build())
        }

        return with(TypeSpec.classBuilder(diffResultClassName)) {
            addModifiers(KModifier.DATA)

            primaryConstructor(primaryConstructorBuilder.build())

            propertySpecs.forEach {
                addProperty(it)
            }

            build()
        }
    }

    private fun getConstructorFieldName(fieldName: String): String {
        return "${fieldName}Changed"
    }
}

//// GENERATED
//data class PersonDiffResult(
//    val firstNameChanged: Boolean,
//    val lastNameChanged: Boolean,
//    val addressChanged: Boolean,
//    val countryChanged: Boolean,
//    val ageChanged: Boolean,
//)