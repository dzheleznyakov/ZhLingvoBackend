package zh.lingvo.domain.changepatterns.helpers

import zh.lingvo.domain.linguisticcategories.VerbTimeTense
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Retention(RUNTIME)
@Target(CLASS)
annotation class VerbTimeTenseHelper(val value: VerbTimeTense, val description: String = "")
