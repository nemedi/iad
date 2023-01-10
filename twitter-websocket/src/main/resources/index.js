window.onload = function() {
	const webSocket = new WebSocket(`ws://${window.location.host}/tweet`);
	webSocket.onmessage = function(message) {
		if (message.data) {
			const tweets = document.getElementById('tweets');
			const span = document.createElement('span');
			span.className = 'text';
			span.innerHTML = message.data;
			tweets.appendChild(span);
			tweets.appendChild(document.createElement('br'));
			tweets.scrollTop = tweets.scrollHeight - tweets.clientHeight;
		}
	};
};