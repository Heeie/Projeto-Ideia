package main.java;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    public static void main(String[] args) {
        // 1. Criar um cliente HTTP padrão
        HttpClient client = HttpClient.newHttpClient();

        // 2. Configurar o pedido HTTP GET para o endereço do servidor
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/"))
                .GET() // Define o método como GET
                .build();

        try {
            System.out.println("A enviar pedido ao servidor...");
            
            // 3. Enviar o pedido e receber a resposta como uma String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Mostrar os dados recebidos no terminal
            System.out.println("\n--- Resposta do Servidor ---");
            System.out.println("Código de Estado HTTP: " + response.statusCode());
            System.out.println("Conteúdo da Resposta: " + response.body());
            
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao ligar ao servidor: " + e.getMessage());
        }
    }
}
