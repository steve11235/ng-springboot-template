import { AppConfig } from '../App/app.config';
import { RestCommService } from './rest-comm.service';

const testUrl: string = "localhost:8080/rs/";
const APP_CONFIG: AppConfig = new AppConfig("RestComm Testing", testUrl);

let restCommService: RestCommService;

describe("RestCommService tests", () => {
    beforeEach(() => {
        this.restCommService = new RestCommService(null, null, APP_CONFIG);
    });

    it("should produce a URL with query params", () => {
        const relativeUrl: string = "entity";
        const queryParams: any = {
            key1: "value!@#",
            key2: "value$%^"
        };
        const fullUrl: string = this.restCommService.assembleFullUrl(relativeUrl, queryParams);

        expect(fullUrl).toEqual("localhost:8080/rs/entitykey1=value!%40%23&key2=value%24%25%5E");

        console.log(fullUrl);
    });
});