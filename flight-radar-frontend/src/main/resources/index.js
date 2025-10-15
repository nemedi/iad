function loadFlights(data, element) {
	const flights = JSON.parse(data);
	element.innerHTML =
		`<table border="0" cellspacing="0" cellpadding="5">
			<tr>
				<th align="right"><b>No.</b></th>
				<th align="left"><b>Flight</b></th>
				<th align="left"><b>From</b></th>
				<th align="left"><b>To</b></th>
				<th align="left"><b>Airline</b></th>
				<th align="left"><b>Aircraft</b></th>
			</tr>`
		+ flights.map((flight, index) =>
			`<tr>
				<td align="right">${index + 1}</td>
				<td align="left">${flight.number}</td>
				<td align="left">${flight.from}</td>
				<td align="left">${flight.to}</td>
				<td align="left">${flight.airline}</td>
				<td align="left">${flight.aircraft}</td>
			</tr>`
		).join('')
		+ `</table>`;
}
window.onload = () => {
	const element = document.getElementById('out');
	element.innerHTML = '<h3>Loading data...</h3>';
	var socket = new WebSocket(window.location.origin.replace(/^http/, 'ws') + '/flights');
	socket.onmessage = (message) => {
		if (message.data instanceof Blob) {
			var reader = new FileReader();
			reader.addEventListener('loadend', (e) => {
				loadFlights(e.srcElement.result, element);
			});
			reader.readAsText(message.data);
		} else {
			loadFlights(message.data, element);
		}
	};
};
