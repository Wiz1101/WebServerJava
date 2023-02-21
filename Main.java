import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

  public static void main(String[] args) throws IOException {
    int port = 0; // PORT
    String dir = ""; // Public Directory


    while (true) {
      try {
        port = Integer.parseInt(args[0]); // PORT
        dir = args[1]; // Public Directory
        dir.toLowerCase();
        if (!dir.equals("public")) {
          System.out.println("ERROR: Incorrect Directory Name!");
          break;
        }
      } catch (Exception e) {
        System.out.println(e);
        System.out.println("ERROR: Please provide valid arguments!");
        break;
      }
      // ~~ SERVER ~~
      try (ServerSocket serverSocket = new ServerSocket(port)) {
        System.out.println("Server started.\n Listening for messages.");
        try (Socket client = serverSocket.accept()) {
          System.out.println("Debug: got new message " + client.toString());
          InputStreamReader isr = new InputStreamReader(client.getInputStream());
          BufferedReader br = new BufferedReader(isr);

          StringBuilder request = new StringBuilder();
          String line; // Temp variable called line that holds one line at a time of our message
          line = br.readLine();
          while (!line.isBlank()) {
            request.append(line + "\r\n");
            line = br.readLine();
          }

          System.out.println("--REQUEST--");
          System.out.println(request);
          String firsline = request.toString().split("\n")[0];
          String resource = firsline.split(" ")[1];
          OutputStream clientOutput = client.getOutputStream();
          FileInputStream file;


          System.out.println("THIS IS  A RESOURCE\n");
          System.out.println(resource);


          try {
            try {
              file = new FileInputStream(dir + resource);
              clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
              clientOutput.write("\r\n".getBytes());
              clientOutput.write(file.readAllBytes());
              file.close();
            } catch (FileNotFoundException e) {
              // 404 Not Found Error
              file = new FileInputStream(dir + "/404.html");
              clientOutput.write("HTTP/1.1 404 Not Found\r\n".getBytes());
              clientOutput.write("\r\n".getBytes());
              clientOutput.write(file.readAllBytes());
            }
          } catch (Exception e) {
            // 500 Internal Server Error
            file = new FileInputStream(dir + "/500.html");
            clientOutput.write("HTTP/1.1 500 Server Error\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(file.readAllBytes());

          }

          // try {
          //   if (resource.equals("/archlinux")) {
          //     file = new FileInputStream(dir + "archlinux.png");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/home")) {
          //     file = new FileInputStream(dir + "/home.html");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/signup")) {
          //     file = new FileInputStream(dir + "/signup.htm");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/index.html")) {
          //     file = new FileInputStream(dir + "/first.html");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/rick")) {
          //     file = new FileInputStream(dir + "/rick.html");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/success")) {
          //     file = new FileInputStream(dir + "/success.html");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/upload")) { // POST REQUEST
          //     file = new FileInputStream(dir + "/upload.html");
          //     clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/500")) {
          //     file = new FileInputStream(dir + "/500.html");
          //     clientOutput.write("HTTP/1.1 500 Server Error\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   } else if (resource.equalsIgnoreCase("/test")) {
          //     String str = String.format("<a href='http://127.0.0.1:%d/rick'><button>Visit Rick!</button></a>\r\n",
          //         port);
          //     // 302 Found
          //     file = new FileInputStream(dir + "/302.html");
          //     clientOutput.write("HTTP/1.1 302 Found\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //     clientOutput.write(str.getBytes());

          //   } else if (resource.equalsIgnoreCase("/test500")) { // Generate an Error for 500
          //     String str = String.format("<a href='http://127.0.0.1:%f/rick'><button>Visit Rick!</button></a>\r\n",
          //         port);
          //     clientOutput.write(str.getBytes());
          //   } else {
          //     // 404 Not Found Error
          //     file = new FileInputStream(dir + "/404.html");
          //     clientOutput.write("HTTP/1.1 404 Not Found\r\n".getBytes());
          //     clientOutput.write("\r\n".getBytes());
          //     clientOutput.write(file.readAllBytes());
          //   }
          //   client.close();
          //   System.err.println("Client connection closed!");

          // } catch (Exception e) {
          //   // 500 Internal Server Error
          //   file = new FileInputStream(dir + "/500.html");
          //   clientOutput.write("HTTP/1.1 500 Server Error\r\n".getBytes());
          //   clientOutput.write("\r\n".getBytes());
          //   clientOutput.write(file.readAllBytes());
          // }

        }

      } catch (Exception e) {
        continue;
      }

    }

}}
