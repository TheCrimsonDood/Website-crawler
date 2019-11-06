const button = document.getElementById('button');
const header = document.getElementById('mainHeader');
const mainArea = document.getElementById('mainArea');
const jsonFile = "../json/nodes.json";
const width = mainArea.offsetWidth;
const height = 481;


// alert('test');
$("#button").click(function() {
    if (!button.classList.contains('active')) {
        button.classList.add('active');
        // alert('hello');

        $.getJSON(jsonFile, function(data) {
            // data.nodes.forEach(node => {
            //     console.log(node);
            // });

            const Graph = ForceGraph()

            Graph.cooldownTime(Infinity)
                (document.getElementById('mainArea'))
                .graphData(data)
                .nodeId('id')
                .nodeVal('val')
                .nodeLabel('id')
                .linkSource('source')
                .linkTarget('target')
                .enableNodeDrag(false)
                .nodeRelSize(2)
                .width(width)
                .height(height)
                .d3AlphaDecay(0.002)
                .d3VelocityDecay(0.4)
                .d3Force('collide', d3.forceCollide(Graph.nodeRelSize()))
                .d3Force('box', () => {
                    const SQUARE_HALF_SIDE = Graph.nodeRelSize() * N * 0.5;
                    nodes.forEach(node => {
                        const x = node.x || 0,
                            y = node.y || 0;
                        // bounce on box walls
                        if (Math.abs(x) > SQUARE_HALF_SIDE) { node.vx *= -1; }
                        if (Math.abs(y) > SQUARE_HALF_SIDE) { node.vy *= -1; }
                    });
                })
                // .backgroundColor("grey");

        });



    } else {

        button.classList.remove('active');

    }

});