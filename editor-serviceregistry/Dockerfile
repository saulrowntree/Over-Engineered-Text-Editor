
# 1st stage, build the app
FROM maven:3.6-jdk-11 as build

WORKDIR /editor-serviceregistry

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
ADD pom.xml .
RUN mvn package -DskipTests

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
ADD src src
RUN mvn package -DskipTests
RUN echo "done!"

# 2nd stage, build the runtime image
FROM openjdk:11-jre-slim
WORKDIR /editor-serviceregistry
# Copy the binary built in the 1st stage
COPY --from=build /editor-serviceregistry/target/editor-serviceregistry.jar ./
COPY --from=build /editor-serviceregistry/target/libs ./libs
COPY --from=build /editor-serviceregistry/target/classes/URLs.properties ./

CMD ["java", "-jar", "editor-serviceregistry.jar"]

EXPOSE 8080
