package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.data.events.Expression
import net.jselby.escapists.data.events.ExpressionValue
import net.jselby.escapists.data.events.expression.ExpressionFunction
import net.jselby.escapists.data.events.interpreter.statement.EmptyToken
import net.jselby.escapists.data.events.interpreter.statement.TokenChildFunction
import net.jselby.escapists.data.events.interpreter.statement.TokenFunction
import java.util.*

/**
 * A parsed statement is a collection of operations and values that can be processed into a value.
 * (i.e, a completed expression)
 */
class ParsedStatement(val statement : Array<Expression>) {
    init {
    }

    fun invoke(interpreter: Interpreter): Any /* String, Int or Double */ {
        //println("Created statement @ ${Thread.currentThread().stackTrace
        //        .toList().drop(1).toString().replace("),", ")\n\tat").replace("[", "\n\tat ").replace("]", "")}");

        // Make a copy of the statement
        // Convert everything into a better representation
        val newList = ArrayList<Any>(); // Token is a operation or function
        for (expression in statement) {
            val value = expression.value;
            if (VERBOSE) {
                print(expression.toString() + " ");
            }
            // Convert the expression Object to a raw value.
            if (value is ExpressionValue.Long) {
                newList.add(value.value);
            } else if (value is ExpressionValue.Double) {
                newList.add(value.value);
            } else if (value is ExpressionValue.String) {
                newList.add(value.value);
            } else if (value !is ExpressionValue.Virgule) {
                newList.add(value);
            }
        }

        // BODMAS
        // Iterate over operators

        // Convert brackets into their own statement
        val list = ArrayList<MutableList<Any>>();

        list.add(newList.toMutableList());
        var stackElement = 0;

        while(stackElement < list.size) {
            var line = list[stackElement];

            // Search for bracket statements, and swap them out
            var i = 0;
            while(i < line.size) {
                val initialObj = line[i];

                if (initialObj is ExpressionValue.Parenthesis) {
                    // It is. Add the additional arguments to the list array, so they can be processed separately.
                    var level = 0;
                    var endIndex = i + 1;
                    while(true) {
                        val obj = line[endIndex];
                        if (obj is ExpressionValue.Parenthesis
                                || (obj is ExpressionFunction && obj.annotation.openEnded)) {
                            level++;
                        } else if (obj is ExpressionValue.EndParenthesis) {
                            if (level == 0) {
                                break;
                            } else {
                                level--;
                            }
                        }
                        endIndex++;
                    }

                    val newId = list.size;

                    val values = line.subList(i + 1, endIndex).toMutableList();

                    list.add(values);
                    if (VERBOSE) println("Preline: $line");

                    line[i] = TokenChildFunction(newId);
                    for (x in (i + 1)..endIndex) {
                        line[x] = EmptyToken();
                    }
                    removeEmptyTokens(line);

                    if (VERBOSE) println("Postline: $line");
                    if (VERBOSE) println("Values: $values");
                } else if (initialObj is ExpressionFunction) {
                    // Cool. Lets check if this requires any arguments.
                    if (initialObj.annotation.openEnded) {
                        // It is. Add the additional arguments to the list array, so they can be processed separately.
                        var level = 0;
                        var endIndex = i + 1;
                        while(true) {
                            val obj = line[endIndex];
                            if (obj is ExpressionValue.Parenthesis
                                    || (obj is ExpressionFunction && obj.annotation.openEnded)) {
                                level++;
                            } else if (obj is ExpressionValue.EndParenthesis) {
                                if (level == 0) {
                                    break;
                                } else {
                                    level--;
                                }
                            }
                            endIndex++;
                        }

                        val newId = list.size;

                        val values = line.subList(i + 1, endIndex).toMutableList();

                        list.add(values);
                        if (VERBOSE) println("Preline: $line");

                        line[i] = TokenFunction(newId, initialObj);
                        for (x in (i + 1)..endIndex) {
                            line[x] = EmptyToken();
                        }
                        removeEmptyTokens(line);

                        if (VERBOSE) println("Postline: $line");
                        if (VERBOSE) println("Values: $values");

                    } else {
                        // It isn't, simply call it, and replace it's index with the return value
                        line[i] = interpreter.callMethod(initialObj.method, initialObj.parameters);
                    }
                }

                i++;
            }

            stackElement++;
        }

        // Iterate over the statement now
        if (VERBOSE) {
            print("Processing: $list... ");
        }

        // Short circuit the statement
        if (list.size == 1 && list[0].size == 1) {
            if (VERBOSE) println("Short circuiting statement: $list");
            return list[0][0];
        }

        val statement = solveSimpleStatement(interpreter, list, list[0]);

        if (VERBOSE) println("Solved: $statement, type: ${statement.javaClass.name}, $list");

        // Return our value
        /*if (statement.contains("\"")) {
            return statement;
        } else {*/
            return statement//.toInt();
        //}
    }

    fun solveSimpleStatement(interpreter: Interpreter, expressionFunctions : ArrayList<MutableList<Any>>,
                             statement : MutableList<Any>, returnArray : Boolean = false) : Any {
        // Check for subcalls first
        for(i in 0..statement.size - 1) {
            val oldValue = statement[i];
            if (oldValue is TokenFunction) {
                val args = solveSimpleStatement(interpreter, expressionFunctions,
                        expressionFunctions[oldValue.id], returnArray = true);
                if (args !is ArrayList<*>) {
                    throw IllegalStateException("solveSimpleStatement() did not return a array.")
                }

                oldValue.callChild.openParams = args.toList();
                statement[i] = interpreter.callMethod(oldValue.callChild.method, oldValue.callChild.parameters)
            } else if (oldValue is TokenChildFunction) {
                statement[i] = solveSimpleStatement(interpreter, expressionFunctions,
                        expressionFunctions[oldValue.id]);
            }
        }

        // A simple statement is a statement without brackets
        // boDMAS

        // Division
        var i = 0;
        while(true) {
            if (statement[i] is ExpressionValue.Divide) {
                //println("/ call $statement");

                val val1 = statement[i - 1];
                val val2 = statement[i + 1];

                if (val1 is Int && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 / val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 / val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Int && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 / val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 / val2;
                    statement[i + 1] = EmptyToken();
                } else {
                    throw IllegalArgumentException("Values aren't a integer in division statement: ${val1.javaClass} : ${val2.javaClass}");
                }

                removeEmptyTokens(statement);
            } else {
                i++;
            }
            if (i == statement.size) {
                break;
            }
        }

        // Multiplication
        i = 0;
        while(true) {
            if (statement[i] is ExpressionValue.Multiply) {
                //println("* call $statement");

                val val1 = statement[i - 1];
                val val2 = statement[i + 1];

                if (val1 is Int && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 * val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 * val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Int && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 * val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 * val2;
                    statement[i + 1] = EmptyToken();
                } else {
                    throw IllegalArgumentException("Values aren't a integer in multiplication statement: ${val1.javaClass} : ${val2.javaClass}");
                }

                removeEmptyTokens(statement);
            } else {
                i++;
            }
            if (i == statement.size) {
                break;
            }
        }

        // Addition
        i = 0;
        while(true) {
            if (statement[i] is ExpressionValue.Plus) {
                //println("+ call $statement");

                val val1 = statement[i - 1];
                val val2 = statement[i + 1];

                if (val1 is Int && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 + val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is String && val2 is String) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 + val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 + val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Int && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 + val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 + val2;
                    statement[i + 1] = EmptyToken();
                }else {
                    throw IllegalArgumentException("Values aren't a valid type in addition statement: ${val1.javaClass} : ${val2.javaClass}");
                }

                removeEmptyTokens(statement);
            } else {
                i++;
            }
            if (i == statement.size) {
                break;
            }
        }

        // Subtraction
        i = 0;
        while(true) {
            if (statement[i] is ExpressionValue.Minus) {
                //println("- call $statement");

                val val1 = statement[i - 1];
                val val2 = statement[i + 1];

                // TODO: Maybe find a better solution for these?
                if (val1 is Int && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 - val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Int) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 - val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Int && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 - val2;
                    statement[i + 1] = EmptyToken();
                } else if (val1 is Double && val2 is Double) {
                    statement[i - 1] = EmptyToken();
                    statement[i] = val1 - val2;
                    statement[i + 1] = EmptyToken();
                } else {
                    throw IllegalArgumentException("Values aren't a integer in subtraction statement: ${val1.javaClass} : ${val2.javaClass}");
                }

                removeEmptyTokens(statement);
            } else {
                i++;
            }
            if (i == statement.size) {
                break;
            }
        }

        if (statement.size != 1 && !returnArray) {
            throw IllegalStateException("Statement execution not completed: $statement");
        }

        if (returnArray) {
            return statement;
        }
        return statement[0];
    }

    private fun removeEmptyTokens(statement: MutableList<Any>) {
        var i = 0;
        while(i < statement.size) {
            if (statement[i].javaClass.name.equals(EmptyToken::class.java.name)) {
                statement.removeAt(i);
            } else {
                i++
            }
        }
    }
}

//fun main(args : Array<String>) {
    //println(ParsedStatement("5 + 5 * (10 + (6 -  5) + (6 - 4)) - (5)").invoke())
    //println(ParsedStatement("5+5*(15/5)*3").invoke())
    //println(ParsedStatement("\"test\" + \" string\"").invoke())
//}