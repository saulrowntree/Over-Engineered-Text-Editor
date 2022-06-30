# server.rb
require 'sinatra'
require 'json'
require_relative 'countvowels'

set :port, 5000
set :bind, '0.0.0.0'

get '/' do
  headers 'Access-Control-Allow-Origin' => '*'
  headers 'Content-Type' => 'application/json'
  begin
    vowel_count = count_vowels(params["text"])
    status 200
    {
      error:false,
      string:"Input text contains #{vowel_count} vowels.",
      answer:vowel_count
    }.to_json
  rescue => NoMethodError
    status 400
    {
      error:true,
      string:"text param was not passed correctly.",
      answer:-1
    }.to_json
  end
end

