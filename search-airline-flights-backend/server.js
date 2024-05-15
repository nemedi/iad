const express = require('express');
const {join, resolve} = require('path');
const {searchAirline, searchFlights, searchFlight} = require('./service');

const PORT = process.env.PORT || 8080;
express()
	.use(express.static(join(resolve(), 'public')))
	.get('/airlines', async (request, response) =>
		response.json(await searchAirline(request.query.name))
	)
	.get('/flights', async (request, response) =>
		response.json(await searchFlights(request.query.airline))
	)
	.get('/flight', async (request, response) =>
		response.json(await searchFlight(request.query.code))
	)
	.listen(PORT, () => console.log(`Server is running on port ${PORT}.`));