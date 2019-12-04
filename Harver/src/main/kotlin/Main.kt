package com.harver.android

import kotlinx.coroutines.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.measureTimeMillis

val words = arrayOf(
    "start", "citizen", "flour", "circle", "petty", "neck", "seem", "lake", "page",
    "color", "ceiling", "angle", "agent", "mild", "touch", "bite", "cause", "finance",
    "greet", "eat", "minor", "echo", "aviation", "baby", "role", "surround", "incapable",
    "refuse", "reliable", "imperial", "outer", "liability", "struggle", "harsh", "coerce",
    "front", "strike", "rage", "casualty", "artist", "ex", "transaction", "parking", "plug",
    "formulate", "press", "kettle", "export", "hiccup", "stem", "exception", "report", "central",
    "cancer", "volunteer", "professional", "teacher", "relax", "trip", "fountain", "effect",
    "news", "mark", "romantic", "policy", "contemporary", "conglomerate", "cotton", "happen",
    "contempt", "joystick", "champagne", "vegetation", "bat", "cylinder", "classify", "even",
    "surgeon", "slip", "private", "fox", "gravity", "aspect", "hypnothize", "generate",
    "miserable", "breakin", "love", "chest", "split", "coach", "pound", "sharp", "battery",
    "cheap", "corpse", "hobby", "mature", "attractive", "rock"
)

class Value(val value: Int, var output: String = "") {
    override fun toString(): String {
        return "$value : $output"
    }
}

suspend fun main() = coroutineScope {

//    launch() {
//        step1()
//    }
//
//    launch() {
//        step2()
//    }
//
//    launch() {
//        step3()
//    }

    callprintFizzBuzzAsyncAcending()//setep bonus 2
    println("")
}


fun callprintFizzBuzzAsyncAcending() {
    runBlocking<Unit> {
        val valueList = ArrayList<Value>()
        val time = measureTimeMillis {
            val defferedlst = ArrayList<Deferred<Any>>()
            for (i in 1..100 step 1) {
                defferedlst.add(async {
                    val word = fizzBuzzAsyncWithError(i)
                    valueList.add(Value(i, word))
                });
            }

            // when
            runBlocking {
                defferedlst.forEach {
                    it.await()
                }
            }
            writeFile(valueList)
        }

        println("Time of excecution $time ms")
    }
}

fun writeFile(valueList: ArrayList<Value>) {

    val myfile = File("input.txt")
    var message = "";
    myfile.printWriter().use { out ->
        valueList.sortedWith(compareBy { it.value }).forEach {
            message += "\n${it}"
            out.println(it);
            println(it)
        }
    }
}

fun sendPost(message: String) {
    val url = URL("http://www.serviceurlhere.com/")

    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        doOutput = true
        val postData: ByteArray = message.toByteArray()
        println("\nSent 'POST' request to URL : $url; Response Code : $responseCode")
        try {
            outputStream.write(postData)
            outputStream.flush()
        } catch (exception: Exception) {
            println(exception.message)
        }
        inputStream.bufferedReader().use {
            it.lines().forEach { line ->
                println(line)
            }
        }
    }
}


fun callprintFizzBuzzAsync() {
    runBlocking<Unit> {
        val time = measureTimeMillis {
            val defferedlst = ArrayList<Deferred<Any>>()
            for (i in 1..100 step 1) {
                defferedlst.add(async {
                    val word = fizzBuzzAsyncWithError(i)
                    println("$i : $word")
                });
            }

            // when
            runBlocking {
                defferedlst.forEach {
                    it.await()
                }
            }
        }

        println("Time of excecution $time ms")
    }
}


suspend fun step1() {
    for (i in 1..100 step 1) {
        val word = getRandomWord()
        println("$i : $word")
    }
}

fun step2() {
    printFizzBuzz()
}

suspend fun step3() {
    printFizzBuzzAsync()
}


suspend fun printFizzBuzzAsync() {
    for (i in 1..100) {
        val result = fizzBuzzAsync(i)
        println("$i: $result")
    }
}

fun printFizzBuzz() {
    for (i in 1..100) {
        val result = fizzBuzz(i)
        println("$i: $result")
    }
}

fun fizzBuzz(value: Int): String {//suspened cuz getRandomWordSyncWithError

    return when {
        value % 3 == 0 && value % 5 == 0 -> "FizzBuzz"
        value % 3 == 0 -> "Fizz"
        value % 5 == 0 -> "Buzz"
        else -> getRandomWordSync()
    }
}

suspend fun fizzBuzzAsync(value: Int): String {//suspened cuz getRandomWordSyncWithError
    try {
        return when {
            value % 3 == 0 && value % 5 == 0 -> "FizzBuzz"
            value % 3 == 0 -> "Fizz"
            value % 5 == 0 -> "Buzz"
            else -> getRandomWordError()
        }
    } catch (exception: Exception) {
        return exception.message!!
    }

}

suspend fun fizzBuzzAsyncWithError(value: Int): String {//suspened cuz getRandomWordSyncWithError
    try {
        return when {
            value % 3 == 0 && value % 5 == 0 -> "FizzBuzz"
            value % 3 == 0 -> "Fizz"
            value % 5 == 0 -> "Buzz"
            else -> getRandomWordError(true)
        }
    } catch (exception: Exception) {
        return exception.message!!
    }

}


fun getRandomWordSync(): String {
    val index = (0..99).random()
    return words[index]
}

suspend fun getRandomWord(slow: Boolean = false): String {
    val index = (0..199).random()
    val word = if (index < words.size) {
        words[index]
    } else {
        throw Exception("It shouldn't break anything!")
    }

    if (slow)
        delay(500)
    return word
}

@Throws
suspend fun getRandomWordError(slow: Boolean = false): String {
    val index = (0..199).random()
    val word = if (index < words.size) {
        words[index]
    } else {
        throw Exception("It shouldn't break anything!")
    }
    if (slow)
        delay(500)
    return word
}