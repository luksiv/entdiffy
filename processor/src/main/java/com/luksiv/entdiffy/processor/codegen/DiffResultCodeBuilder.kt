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
            addKdoc(with(CodeBlock.builder()) {
                addStatement("Diffing utility result class for [${data.modelName}] entity")
                data.modelFields.forEach {
                    addStatement("@param ${getConstructorFieldName(it.fieldName)} boolean whether the [${data.modelName}.${it.fieldName}] is different")
                }
                build()
            })
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