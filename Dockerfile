ARG BUILDER_IMAGE=gradle:7.4.2-jdk17-alpine
ARG PRODUCTION_IMAGE=amazoncorretto:17-alpine3.15

FROM ${BUILDER_IMAGE} as builder

# Download dependencies & build the application
WORKDIR /home/app
COPY build.gradle gradle.properties settings.gradle /home/app/
RUN gradle clean build --no-daemon >/dev/null 2>&1 || true
COPY . .
RUN gradle shadowJar --no-daemon
RUN ls build/libs

# Build a small image for production
FROM ${PRODUCTION_IMAGE} as production

ENV APP_PORT=${PORT}
COPY --from=builder /home/app/build/libs/*-all.jar /home/app/application.jar
EXPOSE ${APP_PORT}
CMD ["java", "-Dmicronaut.environments=prod", "-jar", "/home/app/application.jar"]