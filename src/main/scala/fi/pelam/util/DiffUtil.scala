package fi.pelam.util

import difflib.DiffUtils

import scala.collection.JavaConversions._

object DiffUtil {

  def unifiedDiff(orig: String, modified: String, context: Int = 1): String =
    unifiedDiffLines(orig, modified, context).foldLeft("")(_ + _ + "\n")

  def unifiedDiffLines(orig: String, modified: String, context: Int = 1): Seq[String] = {
    val origLines = orig.split("\n").toList
    val modifiedLines = modified.split("\n").toList
    val patch = DiffUtils.diff(origLines, modifiedLines)
    val diffLines = DiffUtils.generateUnifiedDiff("ORIG", "MODIFIED", origLines, patch, context)
    diffLines
  }

  /**
    * Simplified diff for output without unified diff +++ file, --- file and @@ ... block headers.
    */
  def diff(orig: String, modified: String, context: Int = 1): String = {
    val simplifiedLines = unifiedDiffLines(orig, modified, context) filter {
      case "--- ORIG" => false
      case "+++ MODIFIED" => false
      case s if s.startsWith("@@") && s.endsWith("@@") => false
      case _ => true
    }
    simplifiedLines.foldLeft("")(_ + _ + "\n")
  }
}
