var JSONGenerator = new Object();

JSONGenerator.createJSONFile = function(partyname, articleCount, category, beforeEU, afterEU, whileEU, averageArticleLength, releaseMonth, keywordCount, authorCount, exactWordCount, allKeywordRealtions, keywordOrigin){
 
    var obj = "{'Partei': '"+partyname+"', 'Anzahl der Artikel': '"+articleCount+"', 'Kategorien': '"+category+"', 'Artikel vor der EU-Wahl':'"+beforeEU+"', 'Artikel nach der EU-Wahl':'"+afterEU+"', 'Artikel während der EU-Wahl':'"+whileEU+"', 'Durchschnittliche Artikellänge in Zeichen':'"+averageArticleLength+"', 'Veröffentlichungen nach Monaten':'"+releaseMonth+"', 'Schlagwortanzhal':'"+keywordCount+"', 'Autoren':'"+authorCount+"', 'Wortanzahlen':'"+exactWordCount+", 'Schlagwortzusammenhänge':'"+allKeywordRealtions+"', 'KeywordOrigin' : '"+keywordOrigin+"}";
    var json = JSON.stringify(obj);
    
    return json;


};