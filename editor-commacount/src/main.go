package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"strings"
)

type Response struct {
	Error  bool   `json:"error"`
	String string `json:"string"`
	Answer int    `json:"answer"`
}

func handleRequests() {
	http.HandleFunc("/", handler)
	log.Fatal(http.ListenAndServe(":5000", nil))
}

func handler(w http.ResponseWriter, r *http.Request) {

	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json")

	text, ok := r.URL.Query()["text"]

	if !ok {
		response := Response{Error: true,
			String: "'text' query paramter wasn't included in the request",
			Answer: -1}
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(response)
		return
	}

	stringtext := text[0]
	commacount := strings.Count(stringtext, ",")
	response := Response{Error: false,
		String: "Input text contains " + strconv.Itoa(commacount) + " commas",
		Answer: commacount}
	json.NewEncoder(w).Encode(response)
}

func main() {
	handleRequests()
}
