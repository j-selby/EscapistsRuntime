package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.data.events.Expression
import net.jselby.escapists.data.events.ExpressionValue
import net.jselby.escapists.data.events.expression.ExpressionFunction
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
        // Parse operations, one by one
        // 5 + 5 * (10 + 6) = 85

        // Make a copy of the statement
        println("Entered: $statement");

        var isString = false;

        // Convert everything into a better representation
        val newList = ArrayList<Any>(); // Token is a operation or function
        for (expression in statement) {
            val value = expression.value;
            println("Statement value: " + value.javaClass.name);

            // Convert the expression Object to a raw value.
            if (value is ExpressionValue.Long) {
                newList.add(value.value);
            } else if (value is ExpressionValue.Double) {
                newList.add(value.value);
            } else if (value is ExpressionValue.String) {
                isString = true;
                newList.add(value.value);
            } else if (value is ExpressionFunction) {
                val returnValue = interpreter.callMethod(value.method, value.parameters);
                if (returnValue is String) {
                    isString = true;
                }
                newList.add(returnValue);
            } else {
                newList.add(value);
            }
        }

        // BODMAS
        // Iterate over operators

        // Convert brackets into their own statement
        val list = ArrayList<List<Any>>();

        list.add(newList.toList());

        // TODO: Support bracketed statements
        /*var stackElement = 0;

        while(stackElement < list.size) {
            var line = list[stackElement];

            // Search for bracket statements, and swap them out
            var i = 0;
            while(i < line.size) {
                val initialObj = line[i];

                i++;
            }

        }*/

        println("Conversion: ${statement.toList()} -> $list");

        println("Calling into recursive function...");
        val statement = solveSimpleStatement(list, list[0]);

        println("Solved: $statement, type: ${statement.javaClass.name}");

        // Return our value
        /*if (statement.contains("\"")) {
            return statement;
        } else {*/
            return statement//.toInt();
        //}
    }

    fun solveSimpleStatement(expressionFunctions : ArrayList<List<Any>>, statement : List<Any>) : Any {
        // A simple statement is a statement without brackets
        // boDMAS

        // Functions
        for (i in 0..statement.size - 1) {
            val arg = statement[i];
            if (arg is ExpressionValue.ExtensionCommon) {
                println("Function: ${arg.javaClass.name}");
            } else if (arg is Expression) {
                println("Expression: ${arg.value}");
            } else {
                println("Unknown Entity: ${arg.javaClass.name}");
            }
        }

        // Division
        /*var i = 0;
        while(true) {
            if (args[i].equals("/")) {
                //println("/ call $args");

                val val1 = args[i - 1].toInt();
                val val2 = args[i + 1].toInt();

                args[i - 1] = "";
                args[i] = (val1 / val2).toString();
                args[i + 1] = "";
                args.removeIf { arg -> arg.equals("") };
            } else {
                i++;
            }
            if (i == args.size) {
                break;
            }
        }

        // Multiplication
        i = 0;
        while(true) {
            if (args[i].equals("*")) {
                //println("* call $args");

                val val1 = args[i - 1].toInt();
                val val2 = args[i + 1].toInt();

                args[i - 1] = "";
                args[i] = (val1 * val2).toString();
                args[i + 1] = "";
                args.removeIf { arg -> arg.equals("") };
            } else {
                i++;
            }
            if (i == args.size) {
                break;
            }
        }

        // Addition
        i = 0;
        while(true) {
            if (args[i].equals("+")) {
                //println("+ call $args");

                val val1 = args[i - 1].toInt();
                val val2 = args[i + 1].toInt();

                args[i - 1] = "";
                args[i] = (val1 + val2).toString();
                args[i + 1] = "";
                args.removeIf { arg -> arg.equals("") };
            } else {
                i++;
            }
            if (i == args.size) {
                break;
            }
        }

        // Subtraction
        i = 0;
        while(true) {
            if (args[i].equals("-")) {
               // println("- call $args");

                val val1 = args[i - 1].toInt();
                val val2 = args[i + 1].toInt();

                args[i - 1] = "";
                args[i] = (val1 - val2).toString();
                args[i + 1] = "";
                args.removeIf { arg -> arg.equals("") };
            } else {
                i++;
            }
            if (i == args.size) {
                break;
            }
        }

        var statement = "";
        for ((index, arg) in args.withIndex()) {
            statement += (if (index > 0) " " else "") + arg;
        }

        //println("Solved statement");
        return statement;*/
        return statement.get(0);
    }
}

//fun main(args : Array<String>) {
    //println(ParsedStatement("5 + 5 * (10 + (6 -  5) + (6 - 4)) - (5)").invoke())
    //println(ParsedStatement("5+5*(15/5)*3").invoke())
    //println(ParsedStatement("\"test\" + \" string\"").invoke())
//}