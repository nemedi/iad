import express from 'express';
import {fetchFromRadar, fetchFlight} from 'flightradar24-client';
import airlines from 'airline-codes';
import puppeteer from 'puppeteer';

const PORT = process.env.PORT || 9090;
express()
    .get('/flights', async (request, response) => {
        if (request.query.bounds) {
            const bounds = request.query.bounds.split(',')
                .map(value => parseFloat(value));
            const flights = await fetchFromRadar(...bounds);
            if (flights.length > 0) {
                response.json(flights);
            } else {
                response.send(204);
            }
        } else {
            response.sendStatus(400);
        }
    })
    .get('/flights/:id', async (request, response) => {
        const browser = await puppeteer.launch({headless: true});
        const page = await browser.newPage();
        const remoteResponse = await page.goto(`https://data-live.flightradar24.com/clickhandler/?version=1.5&flight=${request.params.id}`, {
            waitUntil: 'networkidle0'
        });
        response.json(await remoteResponse.json());
        await browser.close();
    })
    .listen(PORT, () => console.log(`Server is running on port ${PORT}.`));