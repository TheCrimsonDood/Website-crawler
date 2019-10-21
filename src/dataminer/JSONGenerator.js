function createJSONFile(partyname, articleCount, category, beforeEU, afterEU, whileEU, averageArticleLength, releaseMonth, keywordCount, authorCount, exactWordCount, allKeywordRealtions, keywordOrigin) {

    // var obj = "{'Partei': '" + partyname + "', 'Anzahl der Artikel': '" + articleCount + "', 'Kategorien': '" + category + "', 'Artikel vor der EU-Wahl':'" + beforeEU + "', 'Artikel nach der EU-Wahl':'" + afterEU + "', 'Artikel während der EU-Wahl':'" + whileEU + "', 'Durchschnittliche Artikellänge in Zeichen':'" + averageArticleLength + "', 'Veröffentlichungen nach Monaten':'" + releaseMonth + "', 'Schlagwortanzhal':'" + keywordCount + "', 'Autoren':'" + authorCount + "', 'Wortanzahlen':'" + exactWordCount + ", 'Schlagwortzusammenhänge':'" + allKeywordRealtions + "', 'KeywordOrigin' : '" + keywordOrigin + "}";
    var obj = "{\n";
    obj = obj + '\"Partei\": \"' + partyname + '\",\n';
    obj = obj + '\"Anzahl der Artikel\":\"' + articleCount + '\",\n';


    obj = obj + '\"Kategorien\":' + category + ',\n';


    obj = obj + '\"Artikel vor der EU-Wahl\":\"' + beforeEU + '\",\n';
    obj = obj + '\"Artikel nach der EU-Wahl\":\"' + afterEU + '\",\n';
    obj = obj + '\"Artikel während der EU-Wahl\":\"' + whileEU + '\",\n';
    obj = obj + '\"Durchschnittliche Artikellänge in Zeichen\":\"' + averageArticleLength + '\",\n';
    obj = obj + '\"Veröffentlichungen nach Monaten\":' + releaseMonth + ',\n';
    obj = obj + '\"Schlagwortanzahl":' + keywordCount + ',\n';
    obj = obj + '\"Autoren\":' + authorCount + ',\n';
    obj = obj + '\"Wortanzahlen\":' + exactWordCount + '\n}';

    // var json = JSON.stringify(obj);

    return obj;


};