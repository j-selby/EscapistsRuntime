package net.jselby.escapists.data.events.interpreter

import java.util.*
import kotlin.text.Regex

/**
 * A parsed statement is a collection of operations and values that can be processed into a value.
 * (i.e, a completed expression)
 */
class ParsedStatement(val statement : String) {
    init {
        // Convert to a static
    }

    fun invoke(): Any /* String, Int or Double */ {
        // Parse operations, one by one
        // 5 + 5 * (10 + 6) = 85

        // Make a copy of the statement
        var statement = statement;
        //println("Entered: $statement");

        // Space out the statement
        var i = 0;
        var newStatement = "";
        while(i < statement.length) {
            if (statement[i] == '/'
                    || statement[i] == '*'
                    || statement[i] == '+'
                    || statement[i] == '-') {
                if (statement[i - 1] != ' ') {
                    newStatement += ' ';
                }
                newStatement += statement[i];
                if (statement[i + 1] != ' ') {
                    newStatement += ' ';
                }
            } else if (statement[i] == '@') {
                // Function call!
                if (statement[i - 1] != ' ') {
                    newStatement += ' ';
                }
                newStatement += statement[i];
                newStatement += statement[i + 1];
                newStatement += statement[i + 2];
                if (statement[i + 3] != ' ') {
                    newStatement += ' ';
                }
                i += 2;
            } else {
                newStatement += statement[i];
            }
            i++;
        }
        statement = newStatement;
        //println("Entered filtered: $statement");

        // BODMAS
        // Iterate over operators

        // Convert brackets into their own statement
        val list = ArrayList<String>();

        list.add(statement);
        var stackElement = 0;

        while(stackElement < list.size) {
            var line = list[stackElement];

            if (line.contains("(")) {
                while (line.contains("(")) {
                    val beginIndex = line.indexOf("(");
                    // Finding the closing bracket could be interesting
                    var endIndex = beginIndex + 1;

                    // Count opening brackets - we want this complete set
                    var openCount = 0;

                    while(true) {
                        if (line[endIndex] == '(') {
                            openCount++;
                        } else if (line[endIndex] == ')') {
                            if (openCount == 0) {
                                // Found our closing bracket
                                break;
                            } else {
                                openCount--;
                            }
                        }
                        endIndex++;
                    }

                    // Swap them out
                    val innerContent = line.substring(beginIndex + 1, endIndex);
                    val newIndex = list.size;
                    list.add(innerContent);
                   // println("$innerContent -> @$newIndex@")
                    line = line.replaceFirst("($innerContent)", "@$newIndex@");
                    //println(line);
                }
                list[stackElement] = line;
            }

            stackElement++;
        }

        println("$statement -> $list");

        //println("Calling into recursive function...");
        statement = solveSimpleStatement(list, list[0]);

        //println("Solved: " + statement);

        // Return our value
        if (statement.contains("\"")) {
            return statement;
        } else {
            return statement//.toInt();
        }
    }

    fun solveSimpleStatement(expressionFunctions : ArrayList<String>, statement : String) : String {
        // A simple statement is a statement without brackets
        // boDMAS
        // Space them out, so we can generate an array
        var args = statement.trim().replace(Regex(" +"), " ").split(" ").toArrayList();

        //println(args);

        // Brackets
        for (i in 0..args.size - 1) {
            val arg = args[i];
            if (arg.startsWith("@") && arg.endsWith("@")) {
                //println("Expression function call $args");
                args[i] = solveSimpleStatement(expressionFunctions, expressionFunctions[arg.replace("@", "").toInt()])
            }
        }
        args.removeIf { arg -> arg.equals("") };

        // Division
        var i = 0;
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
        return statement;
    }
}

fun main(args : Array<String>) {
    println(ParsedStatement("5 + 5 * (10 + (6 -  5) + (6 - 4)) - (5)").invoke())
    println(ParsedStatement("5+5*(15/5)*3").invoke())
    //println(ParsedStatement("\"test\" + \" string\"").invoke())
}