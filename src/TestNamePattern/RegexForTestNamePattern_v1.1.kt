package TestNamePattern

//Readme: Modified By Jianwei

fun getMatch(testName: String): Map<String,String?>{

    var camelCasedTestName = testName

    if(testName.contains("_")){

        val camelMatch= "_([a-z])".toRegex().findAll(camelCasedTestName)

        camelMatch.forEach { camelCasedTestName =
                camelCasedTestName.replace(it.value.toRegex(), it.value[1].toUpperCase().toString()) }
    }

    camelCasedTestName = camelCasedTestName.replace("_","")

    val regexps = listOf(

            "^testGet(?<subject>.+)ed(?<scenario>.+)$",
            "^testCombine(?<subject>.+)_(?<scenario>.+)$",
            "^testCalculate(?<subject>.+)$",
            "^testFrom(?<subject>.+)$",
            "^open(?<subject>.+)$",
            "^test(?<predicate>.+)ed(?<scenario>.+)$",
            "^test(?<predicate>.+)ing(?<subject>.+)$",
            "^testAnd(?<subject>.+)$",
            "^for(?<scenario>.+)$",
            "^testAll(?<scenario>.+)(?<subject>.+)$",
            "^testIs(?<predicate>.+)$",
            "^test(?<subject>.+)Return(?<predicate>.+)$",
            "^fails(?<subject>.+)If(?<predicate>.+)$",
            "^check(?<subject>.+)$",
            "^expands(?<subject>.+)For(?<scenario>.+)$",
            "^expands(?<subject>.+)With(?<scenario>.+)$",
            "^testPresent(?<subject>.+)$",
            "^Verify(?<subject>.+)$",
            "^testLower(?<subject>.+)To(?<scenario>.+)$",
            "^testMap(?<subject>.+)$",
            "^testNo(?<subject>.+)$",
            "^(?!(?:[^_]*_){2})testThrowIf(?<subject>.+)(?<predicate>.+)$",
            "^(?!(?:[^_]*_){2})testVerifyNotNull(?<subject>.+)(?<predicate>.+)$",
            "^remove(?<subject>.+)_(?<scenario>.+)$",
            "^(?!(?:[^_]*_){3})has(?<subject>.+)(?<predicate>.+)(?<scenario>.+)",
            "^test(?<subject>.+)_(?<predicate>.+)$",
            //Other1

            "^test(?<subject>.+)Thrown(?<predicate>.+)$",
            "^test(?<subject>.+)Throws(?<predicate>.+)$",
            "^test(?<subject>.+)ThrowsOn(?<predicate>.+)$",
            "^test(?<subject>.+)Throw(?<predicate>.+)$",
            "^test(?<subject>.+)ThrowOn(?<predicate>.+)$",
            //TryCatchPattern Related

            "^when(?<scenario>.+)Then(?<predicate>.+)$",
            "^when(?<scenario>.+)Should(?<predicate>.+)$",
            "^(?<subject>.+)When(?<scenario>.+)Should(?<predicate>.+)$",
            "^(?<subject>.+)That(?<scenario>.+)Should(?<predicate>.+)$",
            "^(?<subject>.+)That(?<scenario>.+)Should(?<predicate>.+)$",
            "^(?<subject>.+)Must(?<predicate>.+)$",
            "^(?<subject>.+)When(?<scenario>.+)Then(?<predicate>.+)$",
            "^return(?<predicate>.+)When(?<scenario>.+)$",
            "^returns(?<predicate>.+)When(?<scenario>.+)$",
            "^should(?<predicate>.+)When(?<scenario>.+)$",
            "^should(?<predicate>.+)If(?<scenario>.+)$",
            "^has(?<scenario>.+)ShouldBe(?<predicate>.+)$",
            "^(?<subject>.+)Should(?<predicate>.+)When(?<scenario>.+)$",
            "^(?<subject>.+)Must(?<predicate>.+)When(?<scenario>.+)$",
            "^must(?<predicate>.+)For(?<scenario>.+)$",
            "^given(?<subject>.+)When(?<scenario>.+)Then(?<predicate>.+)$",
            "^given(?<subject>.+)When(?<scenario>.+)Should(?<predicate>.+)$",
            "^given(?<subject>.+)Then(?<predicate>.+)When(?<scenario>.+)$",
            "^given(?<subject>.+)Then(?<predicate>.+)$",
            "^test(?<subject>.+)Without(?<scenario>.+)$",
            "^test(?<subject>.+)With(?<scenario>.+)$",
            "^test(?<subject>.+)Uses(?<predicate>.+)$",
            "^should(?<predicate>.+)For(?<subject>.+)$",
            "^should(?<predicate>.+)Given(?<subject>.+)$",
            "^should(?<predicate>.+)Using(?<subject>.+)$",
            "^(?<subject>.+)ShouldContain(?<scenario>.+)$",
            "^test(?<subject>.+)Should(?<predicate>.+)When(?<scenario>.+)$",
            "^test(?<subject>.+)Should(?<predicate>.+)$",
            "^test(?<subject>.+)When(?<scenario>.+)$",
            "^test(?<subject>.+)Using(?<predicate>.+)$",
            "^can(?<predicate>.+)With(?<subject>.+)$",
            "^failsFor(?<subject>.+)$",
            "^test(?<subject>.+)Returns(?<predicate>.+)When(?<scenario>.+)$",
            "^(?<scenario>.+)Shouldbe(?<predicate>.+)$",
            "^(?<scenario>.+)ShouldBe(?<predicate>.+)$",
            "^(?<scenario>.+)Should(?<predicate>.+)$",
            "^test(?<subject>.+)Returns(?<predicate>.+)$",
            "^(?<predicate>.+)When(?<scenario>.+)$",
            "^test(?<subject>.+)After(?<scenario>.+)$",
            "^respondsTo(?<scenario>.+)With(?<predicate>.+)$",
            "^(?!(?:[^_]*_){2})test_(?<subject>.+)$",
            "^(?!(?:[^A-Z]*[A-Z]){4})testIs(?<subject>.+)$",
            "^(?!(?:[^A-Z]*[A-Z]){4})testWas(?<subject>.+)$",
            "^(?!(?:[^A-Z]*[A-Z]){4})testAs(?<subject>.+)$",
            "^(?!(?:[^A-Z]*[A-Z]){4})testTo(?<subject>.+)$",
            "^(?!(?:[^A-Z]*[A-Z]){4})testHas(?<subject>.+)$",
            //Other

            "^(?!(?:[^A-Z]*[A-Z]){3})testWas(?<subject>.+)$",
            "^(?!(?:[^A-Z]*[A-Z]){3})testWas(?<subject>.+)$",
            "^shouldHave(?<subject>.+)=$",
            "^test(?<subject>.+)$",
            "^test(?<subject>.+)_$"
            //Single Entity Related

    ).map { it.toRegex() }

    val match = regexps.mapNotNull { it.matchEntire(camelCasedTestName) }
            .firstOrNull()

    val matchMap = mutableMapOf<String,String?>()
    match?.let {
        try {
            matchMap["subject"] = it.groups["subject"]?.value
        } catch (e: Exception) {
            //empty
        }

        try {
            matchMap["scenario"] = it.groups["scenario"]?.value
        } catch (e: Exception) {
            //empty
        }

        try {
            matchMap["predicate"] = it.groups["predicate"]?.value
        } catch (e: Exception) {
            //empty
        }

    }

    //println(matchList)
    return matchMap
}

fun getEntityMatch(testName: String): Map<String,String?>{

    var camelCasedTestName = testName

    if(testName.contains("_")){

        val camelMatch= "_([a-z])".toRegex().findAll(camelCasedTestName)

        camelMatch.forEach { camelCasedTestName =
                camelCasedTestName.replace(it.value.toRegex(), it.value[1].toUpperCase().toString()) }
    }

    camelCasedTestName = camelCasedTestName.replace("_","")

    val regexps = listOf(
            "^test(?<subject>.+)_$",
            "^test(?<subject>.+)$"
    ).map { it.toRegex() }

    val match = regexps.mapNotNull { it.matchEntire(camelCasedTestName) }
            .firstOrNull()

    val matchMap = mutableMapOf<String,String?>()
    match?.let {
        try {
            matchMap["subject"] = it.groups["subject"]?.value
        } catch (e: Exception) {
            //empty
        }
    }
    return matchMap
}

fun parseName(testName: String): String {

    var camelCasedTestName = testName

    if(testName.contains("_")){

        val camelMatch= "_([a-z])".toRegex().findAll(camelCasedTestName)

        camelMatch.forEach { camelCasedTestName =
                camelCasedTestName.replace(it.value.toRegex(), it.value[1].toUpperCase().toString()) }
    }

    camelCasedTestName = camelCasedTestName.replace("_","")

    return camelCasedTestName
}


