package main

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
)

func TestWithValidInput(t *testing.T) {
	// Create a request to pass to our handler. We don't have any query parameters for now, so we'll
	// pass 'nil' as the third parameter.
	req, err := http.NewRequest("GET", "/", nil)

	q := req.URL.Query()
	q.Add("text", "comma 1, comma 2, comma 3, end.")
	req.URL.RawQuery = q.Encode()

	if err != nil {
		t.Fatal(err)
	}

	// We create a ResponseRecorder (which satisfies http.ResponseWriter) to record the response.
	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(handler)

	// Our handlers satisfy http.Handler, so we can call their ServeHTTP method
	// directly and pass in our Request and ResponseRecorder.
	handler.ServeHTTP(rr, req)

	// Check the status code is what we expect.
	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler returned wrong status code: got %v want %v",
			status, http.StatusOK)
	}

	// Check the response body is what we expect.
	expected := `{"error":false,"string":"Input text contains 3 commas","answer":3}`
	if strings.Trim(rr.Body.String(), " \r\n") != strings.Trim(expected, " \r\n") {
		t.Errorf("handler returned unexpected body: \ngot: \n%v \nwant: \n%v",
			rr.Body.String(), expected)
	}
}

func TestWithoutQueryParam(t *testing.T) {
	// Create a request to pass to our handler. We don't have any query parameters for now, so we'll
	// pass 'nil' as the third parameter.
	req, err := http.NewRequest("GET", "/", nil)
	if err != nil {
		t.Fatal(err)
	}

	// We create a ResponseRecorder (which satisfies http.ResponseWriter) to record the response.
	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(handler)

	// Our handlers satisfy http.Handler, so we can call their ServeHTTP method
	// directly and pass in our Request and ResponseRecorder.
	handler.ServeHTTP(rr, req)

	// Check the status code is what we expect.
	if status := rr.Code; status != http.StatusBadRequest {
		t.Errorf("handler returned wrong status code: got %v want %v",
			status, http.StatusBadRequest)
	}

	// Check the response body is what we expect.
	expected := `{"error":true,"string":"'text' query paramter wasn't included in the request","answer":-1}`
	if strings.Trim(rr.Body.String(), " \r\n") != strings.Trim(expected, " \r\n") {
		t.Errorf("handler returned unexpected body: \ngot: \n%v \nwant: \n%v",
			rr.Body.String(), expected)
	}
}

