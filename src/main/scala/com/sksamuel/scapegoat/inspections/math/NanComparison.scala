package com.sksamuel.scapegoat.inspections.math

import com.sksamuel.scapegoat.{Feedback, Inspection, Levels}

import scala.tools.nsc.Global

/** @author Stephen Samuel */
class NanComparison extends Inspection {

  override def traverser(global: Global, feedback: Feedback): global.Traverser = new global.Traverser {

    import global._
    import definitions._

    def isNan(value: Any): Boolean = {
      value match {
        case d: Double => d.isNaN
        case _ => false
      }
    }

    override def traverse(tree: Tree): Unit = {
      tree match {
        case Apply(Select(lhs, TermName("$eq$eq")), List(Literal(Constant(x))))
          if isFloatingPointType(lhs) && isNan(x) =>
          warn(tree)
        case Apply(Select(Literal(Constant(x)), TermName("$eq$eq")), List(rhs))
          if isFloatingPointType(rhs) && isNan(x) =>
          warn(tree)
        case _ => super.traverse(tree)
      }
    }

    private def isFloatingPointType(lhs: Tree): Boolean = {
      lhs.tpe <:< DoubleClass.tpe || lhs.tpe <:< FloatClass.tpe
    }

    private def warn(tree: Tree) {
      feedback.warn("Nan comparision", tree.pos, Levels.Error,
        "NaN comparision will always fail. Use value.isNan instead.")
    }
  }

}