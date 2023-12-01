import { json } from "./json.js";

var id = getParameterByName('id');
var condition = getParameterByName('c');
var uuid = getParameterByName('u');

console.log(id);

if (id == null) {
    id = getIdFromLocalStorage();
}

const survey = new Survey.Model(json);

survey.locale = "de";

survey.onComplete.add((sender, options) => {

    try {
        
        fetch('<URL>', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "id": id, "condition": condition, "uuid": uuid, "results": sender.data })
        }).then(response => console.log(response.status))

    } catch (error) {
        console.log(error);
    }

});

$(function () {
    $("#surveyContainer").Survey({ model: survey });
});


// Taken from https://stackoverflow.com/questions/53757395/how-to-pass-variable-on-href
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

function getIdFromLocalStorage() {
    return localStorage.getItem("id");
}

