FROM ubuntu:latest AS build

# Atualiza os pacotes e instala o OpenJDK 21 e o Maven
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven

# Define o diretório de trabalho
WORKDIR /app

# Copia o código-fonte para o contêiner
COPY manager /app

# Executa o comando Maven para limpar e construir o projeto
RUN mvn clean install

# Usa uma imagem mais leve do OpenJDK para rodar a aplicação
FROM openjdk:21-jdk-slim

# Define a porta em que a aplicação vai rodar
EXPOSE 8080

# Copia o JAR gerado para a imagem final
COPY --from=build /app/target/todo-3.3.1-SNAPSHOT.jar app.jar

# Define o comando de entrada para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
