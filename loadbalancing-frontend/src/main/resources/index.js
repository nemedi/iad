window.onload = () => {
	var socket = new WebSocket(`ws://${location.host}/service`);
	socket.onmessage = function(message) {
		var response = JSON.parse(message.data);
		document.getElementById('messages').innerHTML +=
			`<li>Server ${response.server} took ${response.timespan} milliseconds to process request.</li>`;
	}
	seInterval(() => socket.send('run'), 5000);
};