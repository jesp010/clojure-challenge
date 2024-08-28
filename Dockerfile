# Dockerfile
FROM clojure:openjdk-11-tools-deps

WORKDIR /app

COPY . /app

RUN clj -M:depstar -m hf.depstar.uberjar calculator.jar

CMD ["java", "-cp", "calculator.jar", "clojure.main", "-m", "calculator.core"]