import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 健身房管理系统后端：JDK 自带 HttpServer，无需第三方框架。 */
public class GymManagementServer {
    private static final int PORT = 8080;
    private static final Path ROOT = Paths.get(".").toAbsolutePath().normalize();
    private static final Path DATA_DIR = ROOT.resolve("data");
    private static final Path MEMBER_FILE = DATA_DIR.resolve("members.txt");
    private static final Path CHECKIN_FILE = DATA_DIR.resolve("checkins.txt");
    private static final List<Member> members = new ArrayList<Member>();
    private static final List<Checkin> checkins = new ArrayList<Checkin>();
    private static final Pattern FIELD = Pattern.compile("\\\"(\\w+)\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"");

    public static void main(String[] args) throws IOException {
        loadData();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/members", GymManagementServer::membersApi);
        server.createContext("/api/checkins", GymManagementServer::checkinsApi);
        server.createContext("/api/classes", GymManagementServer::classesApi);
        server.createContext("/api/dashboard", GymManagementServer::dashboardApi);
        server.createContext("/", GymManagementServer::serveFile);
        server.setExecutor(null);
        server.start();
        System.out.println("健身房管理系统已启动：http://localhost:" + PORT);
        System.out.println("后端接口：/api/members、/api/checkins、/api/classes、/api/dashboard");
    }

    private static synchronized void membersApi(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if ("GET".equals(method)) { sendJson(exchange, 200, memberListJson()); return; }
        if ("POST".equals(method)) {
            Map<String, String> data = fields(readBody(exchange));
            Member m = new Member(data.get("name"), data.get("phone"), data.get("card"), today(), "正常");
            members.add(0, m); saveMembers(); sendJson(exchange, 201, m.json()); return;
        }
        String id = path.substring("/api/members".length()).replace("/", "");
        int index = findMember(id);
        if (index < 0) { sendJson(exchange, 404, "{\"error\":\"会员不存在\"}"); return; }
        if ("PUT".equals(method)) {
            Map<String, String> data = fields(readBody(exchange));
            Member m = members.get(index);
            if (data.containsKey("status")) m.status = data.get("status");
            saveMembers(); sendJson(exchange, 200, m.json()); return;
        }
        if ("DELETE".equals(method)) { members.remove(index); saveMembers(); sendJson(exchange, 200, "{\"success\":true}"); return; }
        sendJson(exchange, 405, "{\"error\":\"不支持的请求\"}");
    }

    private static synchronized void checkinsApi(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) { sendJson(exchange, 200, checkinListJson()); return; }
        if ("POST".equals(exchange.getRequestMethod())) {
            Map<String, String> data = fields(readBody(exchange));
            Checkin c = new Checkin(data.get("name"), data.containsKey("time") ? data.get("time") : now());
            checkins.add(0, c); saveCheckins(); sendJson(exchange, 201, c.json()); return;
        }
        sendJson(exchange, 405, "{\"error\":\"不支持的请求\"}");
    }

    private static void classesApi(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, "[{\"name\":\"瑜伽基础\",\"coach\":\"李教练\",\"time\":\"09:00\",\"current\":12,\"capacity\":20},{\"name\":\"力量训练\",\"coach\":\"王教练\",\"time\":\"11:30\",\"current\":8,\"capacity\":15},{\"name\":\"动感单车\",\"coach\":\"张教练\",\"time\":\"14:00\",\"current\":18,\"capacity\":20}]");
    }

    private static void dashboardApi(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, "{\"memberCount\":" + (1200 + members.size()) + ",\"todayCheckins\":" + (86 + checkins.size()) + ",\"classCount\":24,\"monthIncome\":68420}");
    }

    private static void serveFile(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        if (requestPath.equals("/") || requestPath.isEmpty()) requestPath = "/index.html";
        Path file = ROOT.resolve(requestPath.substring(1)).normalize();
        if (!file.startsWith(ROOT) || !Files.isRegularFile(file)) { sendText(exchange, 404, "404 - 页面不存在"); return; }
        String type = requestPath.endsWith(".html") ? "text/html; charset=UTF-8" : requestPath.endsWith(".css") ? "text/css; charset=UTF-8" : "application/javascript; charset=UTF-8";
        exchange.getResponseHeaders().set("Content-Type", type);
        byte[] body = Files.readAllBytes(file); exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) { out.write(body); }
    }

    private static Map<String, String> fields(String body) {
        Map<String, String> result = new HashMap<String, String>(); Matcher matcher = FIELD.matcher(body);
        while (matcher.find()) result.put(matcher.group(1), matcher.group(2)); return result;
    }
    private static String readBody(HttpExchange e) throws IOException { ByteArrayOutputStream b = new ByteArrayOutputStream(); byte[] buffer = new byte[1024]; int n; while ((n = e.getRequestBody().read(buffer)) != -1) b.write(buffer, 0, n); return new String(b.toByteArray(), StandardCharsets.UTF_8); }
    private static void sendJson(HttpExchange e, int code, String body) throws IOException { e.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8"); e.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); byte[] b=body.getBytes(StandardCharsets.UTF_8); e.sendResponseHeaders(code,b.length); try(OutputStream o=e.getResponseBody()){o.write(b);} }
    private static void sendText(HttpExchange e, int code, String body) throws IOException { byte[] b=body.getBytes(StandardCharsets.UTF_8); e.sendResponseHeaders(code,b.length); try(OutputStream o=e.getResponseBody()){o.write(b);} }
    private static String memberListJson(){StringBuilder s=new StringBuilder("[");for(int i=0;i<members.size();i++){if(i>0)s.append(',');s.append(members.get(i).json());}return s.append(']').toString();}
    private static String checkinListJson(){StringBuilder s=new StringBuilder("[");for(int i=0;i<checkins.size();i++){if(i>0)s.append(',');s.append(checkins.get(i).json());}return s.append(']').toString();}
    private static int findMember(String id){try{return Integer.parseInt(id);}catch(Exception e){return -1;}}
    private static String today(){return "2026-10-14";}
    private static String now(){return "12:00";}
    private static String clean(String s){return s==null?"":s.replace("|", "").replace("\n", "");}

    private static void loadData() throws IOException {
        Files.createDirectories(DATA_DIR);
        if (Files.exists(MEMBER_FILE)) for(String line:Files.readAllLines(MEMBER_FILE,StandardCharsets.UTF_8)){String[] p=line.split("\\|",-1);if(p.length>=5)members.add(new Member(p[0],p[1],p[2],p[3],p[4]));}
        if (Files.exists(CHECKIN_FILE)) for(String line:Files.readAllLines(CHECKIN_FILE,StandardCharsets.UTF_8)){String[] p=line.split("\\|",-1);if(p.length>=2)checkins.add(new Checkin(p[0],p[1]));}
        if(members.isEmpty()){members.add(new Member("张小雨","138****1024","年卡","2025-06-24","正常"));members.add(new Member("李明远","139****2251","季卡","2024-07-02","即将到期"));saveMembers();}
    }
    private static void saveMembers() throws IOException {List<String> lines=new ArrayList<String>();for(Member m:members)lines.add(clean(m.name)+"|"+clean(m.phone)+"|"+clean(m.card)+"|"+clean(m.date)+"|"+clean(m.status));Files.write(MEMBER_FILE,lines,StandardCharsets.UTF_8);}
    private static void saveCheckins() throws IOException {List<String> lines=new ArrayList<String>();for(Checkin c:checkins)lines.add(clean(c.name)+"|"+clean(c.time));Files.write(CHECKIN_FILE,lines,StandardCharsets.UTF_8);}

    private static class Member {String name,phone,card,date,status;Member(String n,String p,String c,String d,String s){name=n;phone=p;card=c;date=d;status=s;}String json(){return "{\"id\":"+members.indexOf(this)+",\"name\":\""+clean(name)+"\",\"phone\":\""+clean(phone)+"\",\"card\":\""+clean(card)+"\",\"date\":\""+clean(date)+"\",\"status\":\""+clean(status)+"\"}";}}
    private static class Checkin {String name,time;Checkin(String n,String t){name=n;time=t;}String json(){return "{\"name\":\""+clean(name)+"\",\"time\":\""+clean(time)+"\"}";}}
}
