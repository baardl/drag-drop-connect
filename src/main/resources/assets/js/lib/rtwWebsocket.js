function setupWebSocket(urlSnippet, onMessageEventHandler) {
    var wshost = location.origin.replace(/^http/, 'ws');
    var websocket = new WebSocket(wshost + urlSnippet);

    websocket.onopen = function () {
        console.log("Setting up Web Socket!");
        websocket.send("JavaScript client connecting!!");
    };

    //The message handler function is configured elsewhere to facilitate reuse
    websocket.onmessage = onMessageEventHandler;

    websocket.onclose = function () {
        console.log("websocket terminated. Will retry automatically in 10.000ms");
        setTimeout(function (){
            setupWebSocket(urlSnippet, onMessageEventHandler);
        }, 10000);
    };
}
