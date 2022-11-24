package com.viewpoint.dangder.util

val emailRegex =  "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+\$".toRegex()