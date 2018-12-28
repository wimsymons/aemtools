import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.identity

plugins {
  id("org.jetbrains.intellij") version "0.4.1"
}

dependencies {
  testCompile(project(":test-framework"))
}

val ideaVersion: String by properties

intellij {
  version = ideaVersion
}
