package com.aemtools.codeinsight.impicitusage

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaSearchMixin

/**
 * Tests for [OSGiServiceImplicitUsageProvider].
 */
class OSGiServiceImplicitUsageProviderTest
  : BaseLightTest(), JavaSearchMixin {

  fun testFelixReference() = textImplicitProvider("org.apache.felix.scr.annotations.Reference")
  fun testOsgiAnnotation() = textImplicitProvider("org.osgi.service.component.annotations.Reference")

  private fun textImplicitProvider(injectAnnotatorFqn: String) = fileCase {
    val annotationName = injectAnnotatorFqn.substringAfterLast(".")
    val annotationPackage = injectAnnotatorFqn.substringBeforeLast(".")

    fixture.addClass("""
      package $annotationPackage;

      import java.lang.annotation.Documented;
      import java.lang.annotation.ElementType;
      import java.lang.annotation.Retention;
      import java.lang.annotation.RetentionPolicy;
      import java.lang.annotation.Target;

      @Target({ElementType.TYPE, ElementType.FIELD})
      @Retention(RetentionPolicy.CLASS)
      @Documented
      public @interface $annotationName {
      }
    """.trimIndent())
    val slingModelClass = fixture.addClass("""
      import $injectAnnotatorFqn;
      
      @Model(adaptables = Resource.class)
      public class ServiceA {
      
          @$annotationName
          private Object serviceB;
      }
    """.trimIndent())

    verify {
      val psiField = slingModelClass.findFieldByName("serviceB", false)
      val implicitUsageProvider = OSGiServiceImplicitUsageProvider()
      assertTrue(implicitUsageProvider.isImplicitWrite(psiField!!))
      assertTrue(implicitUsageProvider.isImplicitUsage(psiField))
      assertFalse(implicitUsageProvider.isImplicitRead(psiField))
    }
  }
}
