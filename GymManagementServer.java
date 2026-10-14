import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** 健身房管理系统的简单 Java 静态文件服务器。 */
public class GymManagementServer {
    private static final int PORT = 8080;
    private static final Path ROOT = Paths.get(".").toAbsolutePath().normalize();
    private static final Map<String, String> TYPES = new HashMap<>();

    public static void main(String[] args) throws IOException {
        TYPES.put(".html", "text/html; charset=UTF-8");
        TYPES.put(".css", "text/css; charset=UTF-8");
        TYPES.put(".js", "application/javascript; charset=UTF-8");
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", GymManagementServer::serveFile);
        server.setExecutor(null);
        server.start();
        System.out.println("健身房管理系统已启动：http://localhost:" + PORT);
        System.out.println("项目目录：" + ROOT);
    }

    private static void serveFile(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        if (requestPath.equals("/") || requestPath.isEmpty()) requestPath = "/index.html";
        Path file = ROOT.resolve(requestPath.substring(1)).normalize();
        if (!file.startsWith(ROOT) || !Files.isRegularFile(file)) {
            byte[] body = "404 - 页面不存在".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, body.length);
            try (OutputStream out = exchange.getResponseBody()) { out.write(body); }
            return;
        }
        String type = TYPES.getOrDefault(getExtension(file.getFileName().toString()), "text/plain; charset=UTF-8");
        exchange.getResponseHeaders().set("Content-Type", type);
        byte[] body = Files.readAllBytes(file);
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) { out.write(body); }
    }

    private static String getExtension(String name) {
        int index = name.lastIndexOf('.');
        return index < 0 ? "" : name.substring(index);
    }
}
