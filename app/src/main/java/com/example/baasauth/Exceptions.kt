package com.example.baasauth


open class WordTooLongException(
    val word: String,
    val maxLength: Int = 10,
    message: String = "La palabra '$word' tiene ${word.length} caracteres (máximo $maxLength)"
) : Exception(message)


class UsernameTooLongException(word: String) :
    WordTooLongException(word, 10, "El nombre de usuario '$word' es demasiado largo")

fun checkWord(word: String) {
    if (word.length > 10) throw WordTooLongException(word)
}




fun checkLightIsOn(isLightOn: Boolean) {
    check(isLightOn) { "La luz no está encendida" }
}


fun requireNonNegative(input: String): Int {
    val number = requireNotNull(input.toIntOrNull()) { "Introduce un número válido" }
    require(number >= 0) { "El número no puede ser negativo: $number" }
    return number
}


fun validateLight(isLightOn: Boolean): String = try {
    checkLightIsOn(isLightOn)
    "OK: la luz está encendida"
} catch (e: IllegalStateException) {
    "Error de estado: ${e.message}"
}

fun validateNumber(input: String): String = try {
    "OK: ${requireNonNegative(input)} es válido"
} catch (e: IllegalArgumentException) {
    "Error de argumento: ${e.message}"
}

fun validateWord(word: String): String = try {
    checkWord(word)
    "OK: '$word' es válida"
} catch (e: WordTooLongException) {
    "Error: ${e.message}"
}