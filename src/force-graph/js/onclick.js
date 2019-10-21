const button = document.getElementById('button');
const header = document.getElementById('mainHeader');


// alert('test');
$("#button").click(function() {
    alert('start');
    if (!button.classList.contains('active')) {
        button.classList.add('active');
    }
});