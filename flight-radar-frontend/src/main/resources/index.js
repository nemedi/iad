function loadFlights(data) {
	const flights = JSON.parse(data);
	document.getElementById('out').innerHTML =
		`<table border="0" cellspacing="0" cellpadding="5">
			<tr>
				<th align="right"><b>No.</b></th>
				<th align="left"><b>Flight</b></th>
				<th align="left"><b>From</b></th>
				<th align="left"><b>To</b></th>
				<th align="left"><b>Airline</b></th>
				<th align="left"><b>Aircraft</b></th>
				<th align="right"><b>Distance</b></th>
			</tr>`
		+ flights.map((flight, index) =>
			`<tr>
				<td align="right">${index + 1}</td>
				<td align="left">${flight.number}</td>
				<td align="left">${flight.from}</td>
				<td align="left">${flight.to}</td>
				<td align="left">${flight.airline}</td>
				<td align="left">${flight.aircraft}</td>
				<td align="right">${flight.distance}</td>
			</tr>`
		).join('')
		+ `</table>`;
}
window.onload = () => {
	var socket = new WebSocket(`ws://${location.host}/flights`)
	socket.onmessage = function(message) {
		if (message.data instanceof Blob) {
			var reader = new FileReader();
			reader.addEventListener('loadend', (e) => {
				loadFlights(e.srcElement.result);
			});
			reader.readAsText(message.data);
		} else {
			loadFlights(message.data);
		}
	}
});
