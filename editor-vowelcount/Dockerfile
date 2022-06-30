FROM ruby:3.0.2 
ADD . /var/www/ruby  
WORKDIR /var/www/ruby  
RUN gem install bundler:2.2.32
RUN bundle install
EXPOSE 5000  

CMD ["bundle", "exec", "rackup", "--host", "0.0.0.0", "-p", "5000"]

