const json = pathToMyDocuments + "Website-crawler/data/0 JSON/nodes.json";

var myGraph = ForceGraph();
myGraph('landkarte')
    .graphData(json);

alert("HEllo");