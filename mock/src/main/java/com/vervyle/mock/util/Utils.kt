package com.vervyle.mock.util

fun Int.toImageNameTemplate(): String {
    if (this > 9999)
        throw RuntimeException("$this is not a valid index for an image")
    return String.format("%04d".format(this))
}