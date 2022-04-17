package com.aemtools.codeinsight.osgiservice.property.provider

import com.aemtools.codeinsight.osgiservice.DsOSGiPropertyLineMarker
import com.aemtools.codeinsight.osgiservice.navigationhandler.FelixOSGiPropertyNavigationHandler
import com.aemtools.common.constant.const
import com.aemtools.lang.java.JavaSearch
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin

class OSGiPropertyDescriptorsProviderTest : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiDsAnnotationsMixin,
    JavaMixin {

  fun testOsgiComponentConfigPropertyMarkerInfo() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("Config.java", """
      package com.test;

      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      
      @ObjectClassDefinition(name = "Configuration")
      public @interface Config {

        @AttributeDefinition(
            name = "Test property",
            description = "Test property description")
        String ${CARET}testProperty();

      }
    """)

    addClass("MyService.java", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};
      import com.test.Config;

      @Component
      @Designate(ocd = Config.class)
      public class MyService {

      }
    """)

    addEmptyOSGiConfigs("/config/com.test.MyService.xml")
    osgiConfig("/config/author/com.test.MyService.xml", """
      testProperty="a"
    """)
    osgiConfig("/config/publish/com.test.MyService.xml", """
      testProperty="b"
    """)
    osgiConfig("/config/publish.prod/com.test.MyService.xml", """
      testProperty="c"
    """)

    verify {
      val osgiOcdPsiClass = JavaSearch.findClass("com.test.Config", project)
          ?: throw AssertionError("Unable to find fixture class!")

      val methodIdentifier = osgiOcdPsiClass.allMethods.first().nameIdentifier
          ?: throw AssertionError("Unable to get OCD method!")

      val marker = DsOSGiPropertyLineMarker().getLineMarkerInfo(methodIdentifier)
          ?: throw AssertionError("Marker is null")
      val navigationHandler = marker.navigationHandler as? FelixOSGiPropertyNavigationHandler
          ?: throw AssertionError("Navigation handler is null")

      val configs = navigationHandler.propertyDescriptors()

      assertEquals(listOf(
          "default       <no value set>",
          "author        a",
          "publish       b",
          "publish, prod c"
      ), configs.map { "${it.mods} ${it.propertyValue}" })
    }
  }
}

