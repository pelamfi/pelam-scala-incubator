package fi.pelam.javafxutil

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

abstract class ScalaToJavafxProps[I, +P <: JavafxProps[I]] extends Function[I, P] {
  override def apply(scalaObj: I): P
}

object ScalaToJavafxProps {

  def forClass[I]: ScalaToJavafxProps[I, JavafxProps[I]] = macro materialize[I]

  def materialize[I: c.WeakTypeTag](c: blackbox.Context): c.Expr[ScalaToJavafxProps[I, JavafxProps[I]]] = {
    import c.universe._

    def getPrimaryConstructorParams(targetType: c.universe.Type): List[c.universe.Symbol] = {
      val targetTypeDecls = targetType.decls
      val primaryConstructor = targetTypeDecls.collectFirst {
        case m: MethodSymbol if m.isPrimaryConstructor => m
      }.get

      val params = primaryConstructor.paramLists.head

      params
    }

    class Field(val constructorParam: c.universe.Symbol) {
      val nameType = constructorParam.name

      val termName = nameType.toTermName

      val nameString = nameType.toString

      val nameCapitalized = nameString.capitalize

      val propTermName = TermName(s"${nameString}Property")

      val getterTermName = TermName(s"get$nameCapitalized")

      val setterTermName = TermName(s"set$nameCapitalized")
    }

    val targetType = weakTypeOf[I]

    val propsTypeName: c.TypeName = TypeName(c.freshName("JavafxProps"))

    val companionObjName: c.TermName = propsTypeName.toTermName

    val params = getPrimaryConstructorParams(targetType)

    val fields = params.map(new Field(_))

    val getters = for (field <- fields) yield {
      q""" def ${field.getterTermName}() = ${field.propTermName}.get() """
    }

    val setters = for (field <- fields) yield {
      q""" def ${field.setterTermName}(i: Int) = ${field.propTermName}.set(i) """
    }

    val generatedProperties = for (field <- fields) yield {
      q""" val ${field.propTermName} = new SimpleIntegerProperty(this, ${field.nameString}, 0) """
    }

    val propertiesListItems = for (field <- fields) yield {
      q"${field.propTermName}"
    }

    val toPropsAssignments = for (field <- fields) yield {
      q"props.${field.propTermName}.set(scalaObj.${field.termName})"
    }

    val toImmutableCtorArgs = for (field <- fields) yield {
      q"${field.propTermName}.get()"
    }


    val tree =
      q"""

    import javafx.beans.property.SimpleIntegerProperty

    class $propsTypeName extends fi.pelam.javafxutil.JavafxProps[$targetType] {

      ..$generatedProperties

      ..$getters

      ..$setters

      override def toScala: $targetType = {
        new $targetType(..$toImmutableCtorArgs)
      }

      override def setFromScala(scalaObj: $targetType): Unit = {
        val props = this
        ..$toPropsAssignments
      }

      override val properties = IndexedSeq(..$propertiesListItems)

      override val numberProperties = IndexedSeq(..$propertiesListItems)

      override def toString = "JavafxProps for " + toScala + " (generated by JavaFxPropsMaker)"
    }

    object $companionObjName extends fi.pelam.javafxutil.ScalaToJavafxProps[$targetType, $propsTypeName] {
      override def apply(scalaObj: $targetType): $propsTypeName = {
        val props = new $propsTypeName()
        ..$toPropsAssignments
        props
      }
    }

    $companionObjName
    """

    // println(s"Macro source code ${targetType}: ${showCode(tree)}")

    c.Expr[ScalaToJavafxProps[I, JavafxProps[I]]](tree)
  }


}