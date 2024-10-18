const SEARCH_AIRLINE_URL = 'https://www.flightradar24.com/v1/search/web/find?type=operator&query=';
const SEARCH_FLIGHTS_URL = 'https://data-cloud.flightradar24.com/zones/fcgi/feed.js?airline=';
const SEARCH_FLIGHT_URL = 'https://data-live.flightradar24.com/clickhandler/?flight=';

async function searchAirline(name) {
	return await (await fetch(SEARCH_AIRLINE_URL + name)).json();
}

async function searchFlights(airline) {
	return await (await fetch(SEARCH_FLIGHTS_URL + airline.toUpperCase())).json();
}

async function searchFlight(code) {
	return await (await fetch(SEARCH_FLIGHT_URL + code)).json();
}

module.exports = {searchAirline, searchFlights, searchFlight};