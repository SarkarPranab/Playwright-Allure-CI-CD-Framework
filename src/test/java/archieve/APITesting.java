package archieve;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.impl.RequestOptionsImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

public class APITesting {

    @Test(description = "Testing API scenarios")
    public void apiTesting() {
//        Authentication Token
        final String BASE_URL = "https://api.eventhub.rahulshettyacademy.com/api";
        final String username = "test.pl@abc.com";
        final String password = "Playwright@Test#13";
        APIRequest apiRequest = Playwright.create().request();
        HashMap<String, String> data = new HashMap<>();
        data.put("email", username);
        data.put("password", password);
        APIResponse apiResponse = apiRequest.newContext().post(BASE_URL+"/auth/login", new RequestOptionsImpl().setData(data));
        Assert.assertTrue(apiResponse.ok());
        String token = JsonPath.read(apiResponse.text(), "$.token");

//        Creating new Event
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("title", "Tech Summit 2026 F");
        eventData.put("description", "A premier technology conference.");
        eventData.put("category", "Conference");
        eventData.put("venue", "Bangalore International Centre");
        eventData.put("city", "Bangalore");
        eventData.put("eventDate", "2026-12-25T09:00:00.000Z");
        eventData.put("price", "1500");
        eventData.put("totalSeats", "500");
        eventData.put("imageUrl", "https://example.com/banner.jpg");
        APIResponse apiEventResponse = apiRequest.newContext().post(BASE_URL+"/events",
                new RequestOptionsImpl()
                        .setHeader("Authorization", "Bearer "+ token)
                        .setData(eventData)
                );
        Assert.assertTrue(apiEventResponse.ok());
        int eventId = JsonPath.read(apiEventResponse.text(),"$.data.id");
        System.out.println(eventId);
//        Book Tickets in created Booking
        HashMap<String, String> bookingData = new HashMap<>();
        bookingData.put("eventId", String.valueOf(eventId));
        bookingData.put("customerName", "Priya Sharma");
        bookingData.put("customerEmail", "priya.sharma@email.com");
        bookingData.put("customerPhone", "+91-9876543210");
        bookingData.put("quantity", "2");
        APIResponse bookingResponse = apiRequest.newContext().post(BASE_URL+"/bookings", new RequestOptionsImpl()
                .setHeader("Authorization", "Bearer "+ token)
                .setData(bookingData));
        Assert.assertTrue(bookingResponse.ok());
        int bookingId = JsonPath.read(bookingResponse.text(), "$.data.id");

//        We will be deleting booking and eventId
      /*  APIResponse deleteResponseBooking = apiRequest.newContext().delete(BASE_URL+"/bookings/"+bookingId, new RequestOptionsImpl()
                .setHeader("Authorization", "Bearer "+ token));
        Assert.assertTrue(deleteResponseBooking.ok());
        APIResponse deleteResponseEvent = apiRequest.newContext().delete(BASE_URL+"/events/"+eventId, new RequestOptionsImpl()
                .setHeader("Authorization", "Bearer "+ token));
        Assert.assertTrue(deleteResponseEvent.ok());*/
    }
}