package com.shlee.githubsearch.data.network

import okhttp3.Headers

class PageLinks(
    headers: Headers
) {

    companion object {
        const val NAME_LINK = "Link"

        const val DELIM_LINKS = ","
        const val DELIM_LINK_PARAM = ";"

        const val META_REL = "rel"
        const val META_PREV = "prev"
        const val META_NEXT = "next"
        const val META_FIRST = "first"
        const val META_LAST = "last"
    }

    var prev: String? = null
    var next: String? = null
    var first: String? = null
    var last: String? = null

    var nextPageKey: Int? = null
        private set
        get() =
            with(next) {
                this ?. run {
                    val segments = this.split("&")
                    if (segments.size < 2) {
                        return null
                    }
                    for (segment in segments) {
                        if (segment.startsWith("page=")) {
                            return segment.removePrefix("page=").toInt()
                        }
                    }
                    return null
                }
            }

    init {
        val linkHeader = headers.get(NAME_LINK)
        linkHeader?.run {
            val links = linkHeader.split(DELIM_LINKS)
            for (link in links) {
                val segment = link.split(DELIM_LINK_PARAM)
                if (segment.size < 2) {
                    continue
                }

                var linkPart = segment[0].trim()
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) {
                    continue
                }
                linkPart = linkPart.substring(1, linkPart.length - 1)

                for (i in 0 until segment.size) {
                    val rel = segment[i].trim().split("=")
                    if (rel.size < 2 || (META_REL != rel[0])) {
                        continue
                    }

                    var relValue = rel[1]
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) {
                        relValue = relValue.substring(1, relValue.length - 1)
                    }

                    when (relValue) {
                        META_PREV -> { prev = linkPart }
                        META_NEXT -> { next = linkPart }
                        META_FIRST -> { first = linkPart }
                        META_LAST -> { last = linkPart }
                    }
                }
            }
        }
    }

}

class Person {
    var name: String = ""
    var age: Int = 0
}

class A {

    // with, let, apply, also, run
    // apply 의 경우 수신객체의 property 초기
    val perter = Person().apply {
        name = "peter"
        age = 10
    }


    // let


    val hexNumberRegex = run {
        val digits = "0-9"
        val sign = "+-"

        Regex("")
    }

    fun test() {
        val sb = StringBuilder().apply {
            append("a")
        }

        with(StringBuilder()) {
            append("a")
            append("b")
            toString()
        }
    }

    fun aa(): String {
        return ""
    }
}

