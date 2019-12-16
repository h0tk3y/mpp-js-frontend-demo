package com.h0tk3y.mpp.sample.client

import react.dom.render
import kotlin.browser.document

fun main() {
    document.addEventListener("DOMContentLoaded", {
        render(document.getElementById("root")) {
            child(App::class) { }
        }
    })
}                