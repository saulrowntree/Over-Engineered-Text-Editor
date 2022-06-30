require_relative 'countvowels'
require_relative 'server'
require 'test/unit'
require 'rack/test'

set :environment, :test

class MyAppTest < Test::Unit::TestCase
  include Rack::Test::Methods

  def app
    Sinatra::Application
  end

  def test_valid_input
    get '/?text=aeiou'
    assert last_response.ok?
    assert_equal '{"error":false,"string":"Input text contains 5 vowels.","answer":5}', last_response.body
  end

  def test_no_text_query_param
    get '/'
    assert last_response.bad_request?
    assert_equal '{"error":true,"string":"text param was not passed correctly.","answer":-1}', last_response.body
  end
end

