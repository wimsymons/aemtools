package com.aemtools.codeinsight.xml

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import com.intellij.codeInsight.daemon.impl.analysis.InsertRequiredAttributeFix

/**
 * Tests for [JcrPrimaryTypeAnnotator].
 */
class JcrPrimaryTypeAnnotatorTest : BaseLightTest() {
  fun `test annotation and fix for _content`() {

    myFixture.configureByText(".content.xml", """<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0"></jcr:root>
    """)

    val fix by notNull<InsertRequiredAttributeFix> {
      myFixture.quickFix(".content.xml")
    }

    myFixture.launchAction(fix)

    myFixture.checkResult("""<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" jcr:primaryType="nt:unstructured"></jcr:root>
    """)
  }

  fun `test annotation and fix for _cq_dialog`() {

    myFixture.configureByText("_cq_dialog.xml", """<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0"></jcr:root>
    """)

    val fix by notNull<InsertRequiredAttributeFix> {
      myFixture.quickFix("_cq_dialog.xml")
    }

    myFixture.launchAction(fix)

    myFixture.checkResult("""<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" jcr:primaryType="nt:unstructured"></jcr:root>
    """)
  }

  fun `test annotation and fix for _cq_editConfig`() {

    myFixture.configureByText("_cq_editConfig.xml", """<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0"></jcr:root>
    """)

    val fix by notNull<InsertRequiredAttributeFix> {
      myFixture.quickFix("_cq_editConfig.xml")
    }

    myFixture.launchAction(fix)

    myFixture.checkResult("""<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" jcr:primaryType="nt:unstructured"></jcr:root>
    """)
  }

  fun `test annotation and fix for dialog`() {

    myFixture.configureByText("dialog.xml", """<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0"></jcr:root>
    """)

    val fix by notNull<InsertRequiredAttributeFix> {
      myFixture.quickFix("dialog.xml")
    }

    myFixture.launchAction(fix)

    myFixture.checkResult("""<?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" jcr:primaryType="nt:unstructured"></jcr:root>
    """)
  }
}
