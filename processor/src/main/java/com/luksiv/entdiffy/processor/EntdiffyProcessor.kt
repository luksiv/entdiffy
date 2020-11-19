package com.luksiv.entdiffy.processor

import com.google.auto.service.AutoService
import com.luksiv.entdiffy.annotations.DiffEntity
import com.luksiv.entdiffy.processor.codegen.DiffResultCodeBuilder
import com.luksiv.entdiffy.processor.codegen.DiffUtilCodeBuilder
import com.luksiv.entdiffy.processor.models.ModelData
import com.luksiv.entdiffy.processor.models.ModelField
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(EntdiffyProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntdiffyProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(DiffEntity::class.java.canonicalName)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment
    ): Boolean {

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: return false

        roundEnvironment.getElementsAnnotatedWith(DiffEntity::class.java)
            .forEach {
                val modelData = getModelData(it)

                val diffUtilFileName = "${modelData.modelName}DiffUtil"
                val diffResultFileName = "${modelData.modelName}DiffResult"

                FileSpec.builder(modelData.packageName, diffResultFileName)
                    .addType(DiffResultCodeBuilder(modelData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))

                FileSpec.builder(modelData.packageName, diffUtilFileName)
                    .addType(DiffUtilCodeBuilder(modelData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))
            }

        return true
    }

    private fun getModelData(elem: Element): ModelData {
        val packageName = processingEnv.elementUtils.getPackageOf(elem).toString()

        val modelName = elem.simpleName.toString() // 2

        val modelFields = elem.enclosedElements.mapNotNull {
            if(it.kind.isField) {
                val elementName = it.simpleName.toString()
                val fieldType = it.asType()

                ModelField(elementName, fieldType)
            } else {
                null
            }
        }
        return ModelData(packageName, modelName, modelFields) // 9
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}