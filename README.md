Scapegoat [![travis image](https://travis-ci.org/sksamuel/scalac-scapegoat-plugin.svg?branch=master)](https://travis-ci.org/sksamuel/scalac-scapegoat-plugin)
==========

Scapegoat is a Scala static code analyzer, what is more colloquially known as a code lint tool or linter. Scapegoat works in a similar vein to Java's [FindBugs](http://findbugs.sourceforge.net/) or [checkstyle](http://checkstyle.sourceforge.net/), or Scala's [Scalastyle](https://github.com/scalastyle/scalastyle).

A static code analyzer is a tool that flag suspicious language usage in code. This can include behavior likely to lead or bugs, non idiomatic usage of a language, or just code that doesn't conform to specified style guidelines.

**What's the difference between this project and Scalastyle (or others)?**

Scalastyle is a similar linting tool which focuses mostly on enforcing style/code standards. There's no problems running multiple analysis tools on the same codebase. In fact it could be beneficial as the total set of possible warnings is the union of the inspections of all the enabled tools. The worst case is that the same warnings might be generated by multiple tools.

### Usage
Scapegoat is developed as a scala compiler plugin, which can then be used inside your build tool.

Latest Release: 0.94.5

See: [sbt-scapegoat](https://github.com/sksamuel/sbt-scapegoat) for SBT integration.

### Reports

Here is sample output from the console during the build for a project with warnings/errors:

```
[warning] [scapegoat] Unused method parameter - org.ensime.util.ClassIterator.scala:46
[warning] [scapegoat] Unused method parameter - org.ensime.util.ClassIterator.scala:137
[warning] [scapegoat] Use of var - org.ensime.util.ClassIterator.scala:22
[warning] [scapegoat] Use of var - org.ensime.util.ClassIterator.scala:157
[   info] [scapegoat]: Inspecting compilation unit [FileUtil.scala]
[warning] [scapegoat] Empty if statement - org.ensime.util.FileUtil.scala:157
[warning] [scapegoat] Expression as statement - org.ensime.util.FileUtil.scala:180

```

And if you prefer a prettier report, here is a screen shot of the type of HTML report scapegoat generates:

![screenshot](https://raw.githubusercontent.com/sksamuel/scapegoat/master/screenshot1.png)

### False positives

Please note that scapegoat is a new project. While it's been tested on some common open source projects, there is still a good chance you'll find false positives. Please open up issues if you run into these so we can fix them.

### Changelog

* **0.94.0** - Fixed some more false positives. Added MethodNames inspection, StripMarginOnRegex inspection, and VariableShadowing inspection (the latter being a work in progress, please report feedback).

* **0.93.2** - Fixed false positives.

* **0.93.1** - #67 fixed var args in duplicate map check, #66 ignoring methods returning nothing when checking for unused params, #69 fixed extended classes false pos, #73 Removed incorrect inspection, #64 updated supression to use tree.symbol.isSynthetic instead of mods.synth, Merge pull request #77 from paulp/psp, Give access to the inspection logic through the sbt console, #76 Improve the contains test.

* **0.93.0** - Added ability to define multiple traversers that run in seperate phases of the compiler, #58 Updated supression to work on objects and classes, #60 handling case objects in suspcious match on class object, Allow all inspections to be disabled, other fixes

* **0.92.2** - Added debug option, Made summary optional and disabled in tests, Improved var could be val #54, Split null inspections into assignment and invocation #53, Bumped count on operators to > 2, loads of fixes, loads of verboseness removed.

* **0.92.1** - Fixed a load of false positives.

* **0.92.0** - Added swallowed exception inspection, Added public finalizer inspection, Added use expm1(x) instead of exp(x) - 1 inspection, Added use log1p(x) instead of log(x + 1) inspection, Added use log10(x) instead of log(x)/log(10) inspection, Added use cbrt inspection

* **0.91.0** - Updated logging format to include less [scapegoat] everywhere, Addded scala.math and java.StrictMath to useSqrt, Added ignored files patterns option, Added wildcard import inspection, Added comparison to empty set inspection

* **0.90.17** - Added looks like interpolated string inspection, Added SuspiciousMatchOnClassObject inspection, Updated varuse to not warn on vars in actors #46, Added comparison to empty list inspection, #37 Changed emptyinterpolated string to error, #37 Fixed warning on max parameters

* **0.90.14** - Bunch of bug fixes for false positives. No new inspections.

* **0.90.13** - Fixed NPE in VarClosure inspection, Added Object Names inspection, Added classnames inspection, Added avoid to minus one inspection.

* **0.90.12** - New inspections: unnecessary override, duplicate import, pointless type bounds, max parameters, var closure, method returning any. Updated repeated case body to ignore bodies with two or less statements #28. Removed false positives on getter/setter #27.

* **0.90.11** - Added empty for inspection, AnyUse inspection, Added ArrayEquals inspection, Added double negation inspection, Disabled expession as statement inspection by default, Added avoid operator overload inspection, #25 improving repeated case bodies, Added lonely sealed trait. Added postInspection call to inspections

* **0.90.10** - Added type shadowing inspection, var could be val inspection, unreachable catch inspection and unnecessary toString inspection

* **0.90.09** - Added new inspections: bounded by final type, empty while block, prefer vector empty, finalizer without super, impossible option size condition, filter dot head, repeated case body. Added `infos` to HTML output header

* **0.90.8** - Fixed erroneous partial functions inspection. Added inspection for empty case classe. Changed levels in output to lowercase. Added console output option. Fixed seq empty on non empty seq. Changed return usage to info. Fixed odd issue with empty tree. Changed unused parameter in override to be info. Ignoring all synthetic method added to case classes. Fixed while(true) being detected by ConstantIf

### Inspections

There are currently 107 inspections. An overview list is given, followed by a more detailed description of each inspection after the list (todo: finish rest of detailed descriptions)

|Name|Brief Description|
|----|-----------|
| ArrayEquals | Checks for comparison of arrays using `==` which will always return false |
| ArraysInFormat| Checks for arrays passed to String.format |
| ArraysToString| Checks for explicit toString calls on arrays |
| AvoidOperatorOverload | Checks for mental symbolic method names | 
| AvoidSizeEqualsZero | Traversable.size can be slow for some data structure, prefer .isEmpty |
| AvoidSizeNotEqualsZero | Traversable.size can be slow for some data structure, prefer .nonEmpty |
| AvoidToMinusOne | Checks for loops that use `x to n-1` instead of `x until n` |
| AsInstanceOf| Checks for use of `asInstanceOf` |
| BigDecimalDoubleConstructor| Checks for use of `BigDecimal(double)` which can be unsafe |
| BoundedByFinalType | Looks for types with upper bounds of a final type |
| BrokenOddness| checks for a % 2 == 1 for oddness because this fails on negative numbers |
| CatchNpe| Checks for try blocks that catch null pointer exceptions |
| CatchThrowable | Checks for try blocks that catch Throwable |
| ClassNames | Ensures class names adhere to the style guidelines |
| CollectionNamingConfusion| Checks for variables that are confusingly named |
| CollectionNegativeIndex| Checks for negative access on a sequence eg `list.get(-1)` |
| CollectionPromotionToAny| Checks for collection operations that promote the collection to `Any` |
| ComparingFloatingPointTypes| Checks for equality checks on floating point types |
| ComparingUnrelatedTypes| Checks for equality comparisons that cannot succeed |
| ComparisonToEmptyList | Checks for code like `a == List()` or `a == Nil` |
| ComparisonToEmptySet | Checks for code like `a == Set()` or `a == Set.empty` |
| ComparisonWithSelf| Checks for equality checks with itself |
| ConstantIf| Checks for code where the if condition compiles to a constant |
| DivideByOne| Checks for divide by one, which always returns the original value |
| DoubleNegation | Checks for code like `!(!b)` |
| DuplicateImport | Checks for import statements that import the same selector |
| DuplicateMapKey| Checks for duplicate key names in Map literals |
| DuplicateSetValue | Checks for duplicate values in set literals |
| EitherGet| Checks for use of .get on Left or Right |
| EmptyCaseClass | Checks for case classes like `case class Faceman()` |
| EmptyCatchBlock| Checks for swallowing exceptions |
| EmptyFor | Checks for empty `for` loops |
| EmptyIfBlock| Checks for empty `if` blocks |
| EmptyInterpolatedString| Looks for interpolated strings that have no arguments |
| EmptyMethod| Looks for empty methods |
| EmptySynchronizedBlock| Looks for empty synchronized blocks |
| EmptyTryBlock| Looks for empty try blocks |
| EmptyWhileBlock | Looks for empty while loops |
| ExistsSimplifableToContains | `exists(x => x == b)` replaceable with `contains(b)` |
| FilterDotHead| `.filter(x => ).head` can be replaced with `find(x => ) match { .. } ` |
| FilterDotHeadOption| `.filter(x =>).headOption` can be replaced with `find(x => )` |
| FilterDotIsEmpty| `.filter(x => Bool).isEmpty` can be replaced with `!exists(x => Bool)` |
| FilterOptionAndGet| `.filter(_.isDefined).map(_.get)` can be replaced with `flatten` |
| FilterDotSize| `.filter(x => Bool).size` can be replaced more concisely with with `count(x => Bool)` |
| FinalizerWithoutSuper | Checks for overriden finalizers that do not call super |
| FindDotIsDefined| `find(x => Bool).isDefined` can be replaced with `exist(x => Bool)` |
| IllegalFormatString| Looks for invalid format strings |
| IncorrectlyNamedExceptions| Checks for exceptions that are not called *Exception and vice versa |
| IncorrectNumberOfArgsToFormat| Checks for wrong number of arguments to `String.format` |
| InvalidRegex| Checks for invalid regex literals |
| ImpossibleOptionSizeCondition | Checks for code like `option.size > 2` which can never be true |
| IsInstanceOf| Checks for use of `isInstanceOf` |
| JavaConversionsUse| Checks for use of implicit Java conversions |
| ListAppend | Checks for List :+ which is O(n) |
| ListSize| Checks for `List.size` which is O(n). |
| LooksLikeInterpolatedString | Finds strings that look like they should be interpolated but are not |
| LonelySealedTrait | Checks for sealed traits which have no implementation |
| MaxParameters | Checks for methods that have over 10 parameters |
| MethodNames | Warns on method names that don't adhere to the Scala style guidelines |
| MethodReturningAny | Checks for defs that are defined or inferred to return `Any` |
| ModOne| Checks for `x % 1` which will always return `0` |
| NanComparison| Checks for `x == Double.NaN` which will always fail |
| NegationIsEmpty | `!Traversable.isEmpty` can be replaced with `Traversable.nonEmpty` |
| NegationNonEmpty | `!Traversable.nonEmpty` can be replaced with `Traversable.isEmpty` |
| NullUse| Checks for use of `null` |
| ObjectNames | Ensures object names adhere to the Scala style guidelines |
| OptionGet| Checks for `Option.get` |
| OptionSize| Checks for `Option.size` |
| ParameterlessMethodReturnsUnit| Checks for `def foo : Unit` |
| PartialFunctionInsteadOfMatch | Warns when you could use a partial function directly instead of a match block |
| PointlessTypeBounds | Finds type bounds of the form `[A <: Any]` or `[A >: Nothing]`
| PreferSeqEmpty| Checks for Seq() when could use Seq.empty |
| PreferSetEmpty| Checks for Set() when could use Set.empty |
| PreferVectorEmpty| Checks for Vector() when could use Vector.empty |
| ProductWithSerializableInferred| Checks for vals that have `Product with Serializable` as their inferred type |
| PublicFinalizer | Checks for overriden finalizes that are public |
| RedundantFinalizer| Checks for empty finalizers. |
| RepeatedCaseBody | Checks for case statements which have the same body |
| SimplifyBooleanExpression | `b == false` can be simplified to `!b` |
| StripMarginOnRegex | Checks for .stripMargin on regex strings that contain a pipe |
| SubstringZero | Checks for `String.substring(0)` |
| SuspiciousMatchOnClassObject | Finds code where matching is taking place on class literals |
| SwallowedException | Finds catch blocks that don't handle caught exceptions | 
| SwapSortFilter| `sort.filter` can be replaced with `filter.sort` for performance |
| TraversableHead| Looks for unsafe usage of `Traversable.head` |
| TryGet| Checks for use of `Try.get` |
| TypeShadowing | Checks for shadowed type parameters in methods |
| UnnecessaryIf| Checks for code like `if (expr) true else false` |
| UnneccessryOverride | Checks for code that overrides parent method but simply calls super |
| UnnecessaryReturnUse| Checks for use of `return` keyword in blocks |
| UnnecessaryToInt | Checks for unnecessary `toInt` on instances of Int |
| UnnecessaryToString | Checks for unnecessary `toString` on instances of String |
| UnreachableCatch | Checks for catch clauses that cannot be reached |
| UnsafeContains| Checks for `List.contains(value)` for invalid types |
| UnusedMethodParameter| Checks for unused method parameters |
| UseCbrt| Checks for use of `math.pow` for calculating `math.cbrt` |
| UseExpM1| Checks for use of `math.exp(x) - 1` instead of `math.expm1(x)` |
| UseLog10| Checks for use of `math.log(x)/math.log(10)` instead of `math.log10(x)` |
| UseLog1P| Checks for use of `math.log(x + 1)` instead of `math.log1p(x)` |
| UseSqrt| Checks for use of `math.pow` for calculating `math.sqrt` |
| VarClosure | Finds closures that reference var |
| VarCouldBeVal | Checks for `var`s that could be declared as `val`s |
| VariableShadowing | Warns for variables that shadow variables or parameters in an outer scope with the same name |
| VarUse| Checks for use of `var` |
| WhileTrue| Checks for code that uses a `while(true)` or `do { } while(true)` block. |
| WildcardImport | Checks for wildcard imports |
| ZeroNumerator | Checks for dividing by 0 by a number, eg `0 / x` which will always return `0` |

##### Arrays to string

Checks for explicit toString calls on arrays. Since toString on an array does not perform a deep toString, like say scala's List, this is usually a mistake.

##### ComparingUnrelatedTypes

Checks for equality comparisons that cannot succeed because the types are unrelated. Eg `"string" == BigDecimal(1.0)`. The scala compiler has a less strict version of this inspection.

##### ConstantIf

Checks for if statements where the condition is always true or false. Not only checks for the boolean literals, but also any expression that the compiler is able to turn into a constant value. Eg, `if (0 < 1) then else that`

##### IllegalFormatString

Checks for a format string that is not invalid, such as invalid conversions, invalid flags, etc. Eg, `"% s"`, `"%qs"`, `%.-4f"`

##### IncorrectNumberOfArgsToFormat

Checks for an incorrect number of arguments to String.format. Eg, `"%s %s %f".format("need", "three")` flags an error because the format string specifies 3 parameters but the call only provides 2.

##### InvalidRegex

Checks for invalid regex literals that would fail at compile time. Either dangling metacharacters, or unclosed escape characters, etc that kind of thing.

##### List size

Checks for .size on an instance of List. Eg, `val a = List(1,2,3); a.size`

*Rationale* List.size is O(n) so for performance reasons if .size is needed on a list that could be large, consider using an alternative with O(1), eg Array, Vector or ListBuffer.

##### Redundant finalizer

Checks for empty finalizers. This is redundant code and should be removed. Eg, `override def finalize : Unit = { }`

##### PreferSetEmpty

Indicates where code using Set() could be replaced with Set.empty. Set() instantiates a new instance each time it is invoked, whereas Set.emtpy returns a pre-instantiated instance.

##### UnnecessaryReturnUse

Checks for use of return in a function or method. Since the final expression of a block is always the return value, using return is unnecessary. Eg, `def foo = { println("hello"); return 12; }`

##### UnreachableCatch

Checks for catch clauses that cannot be reached. This means the exception is dead and if you want that exception to take precedence you should move up further up the case list.

##### UnsafeContains

Checks for `List.contains(value)` for invalid types. The method for contains accepts any types. This inspection finds situations when you have a list of type A and you are checking for contains on type B which cannot hold.

##### While true

Checks for code that uses a `while(true)` or `do { } while(true)` block.

*Rationale*: This type of code is usually not meant for production as it will not return normally. If you need to loop until interrupted then consider using a flag.

### Other static analysis tools:

* ScalaStyle (Scala) - https://github.com/scalastyle/scalastyle/wiki
* Linter (Scala) - https://github.com/HairyFotr/linter
* WartRemover (Scala) - https://github.com/typelevel/wartremover
* Findbugs (JVM) - http://findbugs.sourceforge.net/bugDescriptions.html
* Fb-contrib (JVM) - http://fb-contrib.sourceforge.net/
* CheckStyle (Java) - http://checkstyle.sourceforge.net/availablechecks.html
* PMD (Java) - http://pmd.sourceforge.net/pmd-5.0.3/rules/index.html
* Error-prone (Java) - https://code.google.com/p/error-prone/wiki/BugPatterns
* CodeNarc (Groovy) - http://codenarc.sourceforge.net/codenarc-rule-index.html
* PVS-Studio (C++) - http://www.viva64.com/en/d/
* Coverity (C++) - http://www.slideshare.net/Coverity/static-analysis-primer-22874326 (6,7)
* CppCheck (C++) - http://cppcheck.sourceforge.net/
* OCLint (C++/ObjC) - http://docs.oclint.org/en/dev/rules/index.html
* JSHint (Javascript) - http://www.jshint.com/
* JavascriptLint (Javascript) - http://www.javascriptlint.com/
* ClosureLinter (Javascript) - https://developers.google.com/closure/utilities/
