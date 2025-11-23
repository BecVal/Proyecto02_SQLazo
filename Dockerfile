FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends findutils && rm -rf /var/lib/apt/lists/*
COPY src/main/java ./src
RUN mkdir -p out && find src -name '*.java' -print0 | xargs -0 -r javac -d out
CMD ["java", "-cp", "out", "mx.unam.ciencias.myp.butchery.Main"]