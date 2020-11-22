package com.luksiv.entdiffy.processor

import com.google.auto.service.AutoService
import com.luksiv.entdiffy.annotations.DiffEntity
import com.luksiv.entdiffy.processor.codegen.DiffResultCodeBuilder
import com.luksiv.entdiffy.processor.codegen.DiffUtilCodeBuilder
import com.luksiv.entdiffy.processor.codegen.DiffUtilExtensionsCodeBuilder
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

        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: return false

        roundEnvironment.getElementsAnnotatedWith(DiffEntity::class.java)
            .forEach {
                val modelData = getModelData(it)

                val diffUtilFileName = "${modelData.modelName}DiffUtil"
                val diffUtilExtensionsFileName = "${modelData.modelName}DiffUtilExtensions"
                val diffResultFileName = "${modelData.modelName}DiffResult"

                FileSpec.builder(modelData.packageName, diffResultFileName)
                    .addComment(LICENSE_HEADER)
                    .addType(DiffResultCodeBuilder(modelData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))

                FileSpec.builder(modelData.packageName, diffUtilFileName)
                    .addComment(LICENSE_HEADER)
                    .addType(DiffUtilCodeBuilder(modelData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))

                FileSpec.builder(modelData.packageName, diffUtilExtensionsFileName)
                    .addComment(LICENSE_HEADER)
                    .addType(DiffUtilExtensionsCodeBuilder(modelData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))
            }

        return true
    }

    private fun getModelData(elem: Element): ModelData {
        val packageName = processingEnv.elementUtils.getPackageOf(elem).toString()

        val modelName = elem.simpleName.toString() // 2

        val modelFields = elem.enclosedElements.mapNotNull {
            if (it.kind.isField) {
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

    private val LICENSE_HEADER =
        """
*****************************************************************************
MIT License

Copyright (c) 2020 Lukas Šivickas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*******************************************************************************
""".trim()
}