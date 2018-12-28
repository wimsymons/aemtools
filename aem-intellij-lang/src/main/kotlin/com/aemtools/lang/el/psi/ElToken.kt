package com.aemtools.lang.el.psi

import com.aemtools.lang.el.ElLanguage
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class ElToken(debugName: String)
  : IElementType(debugName, ElLanguage)
