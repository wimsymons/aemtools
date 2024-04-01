package com.aemtools.codeinsight.impicitusage

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaSearchMixin

/**
 * Tests for [SlingModelFieldsImplicitUsageProvider].
 */
class SlingModelFieldsImplicitUsageProviderTest
  : BaseLightTest(), JavaSearchMixin {

  fun testValueMapValue() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.ValueMapValue")
  fun testScriptVariable() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.ScriptVariable")
  fun testChildResource() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.ChildResource")
  fun testRequestAttribute() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.RequestAttribute")
  fun testResourcePath() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.ResourcePath")
  fun testOSGiService() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.OSGiService")
  fun testContextAwareConfiguration() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.ContextAwareConfiguration")
  fun testSelf() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.Self")
  fun testSlingObject() = textImplicitProvider("org.apache.sling.models.annotations.injectorspecific.SlingObject")
  fun testParentResourceValueMapValue() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.ParentResourceValueMapValue")
  fun testAemObject() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.AemObject")
  fun testChildResourceFromRequest() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.ChildResourceFromRequest")
  fun testHierarchicalPageProperty() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.HierarchicalPageProperty")
  fun testI18N() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.I18N")
  fun testJsonValueMapValue() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.JsonValueMapValue")
  fun testSharedValueMapValue() = textImplicitProvider("com.adobe.acs.commons.models.injectors.annotation.SharedValueMapValue")
  fun testInject() = textImplicitProvider("javax.inject.Inject")

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

      @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
      @Retention(RetentionPolicy.RUNTIME)
      @Documented
      public @interface $annotationName {
      }
    """.trimIndent())
    val slingModelClass = fixture.addClass("""
      import org.apache.sling.api.resource.*;
      import org.apache.sling.models.annotations.*;
      import $injectAnnotatorFqn;
      
      @Model(adaptables = Resource.class)
      public class SlingModelA {
      
          @$annotationName
          private Object field;
      }
    """.trimIndent())

    verify {
      val psiField = slingModelClass.findFieldByName("field", false)
      val implicitUsageProvider = SlingModelFieldsImplicitUsageProvider()
      assertTrue(implicitUsageProvider.isImplicitWrite(psiField!!))
      assertTrue(implicitUsageProvider.isImplicitUsage(psiField))
      assertFalse(implicitUsageProvider.isImplicitRead(psiField))
    }
  }
}

