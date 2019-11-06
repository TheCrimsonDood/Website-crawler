const button = document.getElementById('button');
const header = document.getElementById('mainHeader');
const mainArea = document.getElementById('mainArea');
const jsonFile = "../json/test.json";
const width = mainArea.offsetWidth;
const height = mainArea.offsetHeight;


// alert('test');
$("#button").click(function () {
    if (!button.classList.contains('active')) {
        button.classList.add('active');
        // alert('hello');

        $.getJSON(jsonFile, function (data) {
            // data.nodes.forEach(node => {
            //     console.log(node);
            // });
            var jsonFile;
            const Graph = ForceGraph()

            (document.getElementById('mainArea'))
            .graphData(jsonFile)
                .nodeId('id')
                .nodeVal('val')
                .nodeLabel('id')
                .linkSource('source')
                .linkTarget('target')
                .enableNodeDrag(false)
                .nodeRelSize(2)
                .width(width)
                .height(height)
                .d3AlphaDecay(0.04)
                .d3VelocityDecay(0.4)
                .d3Force('collide', d3.forceCollide(Graph.nodeRelSize()))
            // .d3Force('box', () => {
            //     const SQUARE_HALF_SIDE = Graph.nodeRelSize() * N * 0.5;
            //     nodes.forEach(node => {
            //         const x = node.x || 0,
            //             y = node.y || 0;
            //         // bounce on box walls
            //         if (Math.abs(x) > SQUARE_HALF_SIDE) { node.vx *= -1; }
            //         if (Math.abs(y) > SQUARE_HALF_SIDE) { node.vy *= -1; }
            //     });
            // })
            // .backgroundColor("grey");
            Graph.cooldownTime(Infinity)
                .d3AlphaDecay(10)
                .d3VelocityDecay(0)

                data.nodes.forEach(node => {
                const { nodes , links } = Graph.graphData();
                Graph.graphData({
                    nodes: [...nodes, {node]
                })
                });

        });



    } else {

        button.classList.remove('active');

    }

});