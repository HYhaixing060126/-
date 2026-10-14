package com.gym;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GymApiController {
    private final JdbcTemplate db;
    private final Map<String, Map<String,Object>> sessions = new ConcurrentHashMap<>();
    public GymApiController(JdbcTemplate db) { this.db = db; }

    @PostMapping("/auth/login")
    public Map<String,Object> login(@RequestBody Map<String,String> body) {
        List<Map<String,Object>> rows = db.queryForList("select id,username,password,real_name,role from sys_user where username=?", body.get("username"));
        if (rows.isEmpty() || !rows.get(0).get("password").toString().equals(body.get("password"))) throw new IllegalArgumentException("用户名或密码错误");
        Map<String,Object> user = new HashMap<>(rows.get(0)); user.remove("password"); String token = UUID.randomUUID().toString(); sessions.put(token,user);
        Map<String,Object> result = new HashMap<>(); result.put("token",token); result.put("user",user); return result;
    }
    @PostMapping("/auth/logout") public Map<String,Object> logout(@RequestHeader(value="X-Token", required=false) String token){if(token!=null)sessions.remove(token);return ok();}
    @GetMapping("/auth/me") public Map<String,Object> me(@RequestHeader("X-Token") String token){Map<String,Object> u=sessions.get(token);if(u==null)throw new IllegalArgumentException("登录已过期");return u;}

    @GetMapping("/members") public List<Map<String,Object>> members(){return db.queryForList("select id,name,phone,card_type card,date_format(expire_date,'%Y-%m-%d') date,status from member order by id desc");}
    @PostMapping("/members") public Map<String,Object> addMember(@RequestBody Map<String,Object> b){db.update("insert into member(name,phone,card_type,expire_date,status) values(?,?,?,?,?)",b.get("name"),b.get("phone"),b.get("card"),b.get("date"),"正常");return ok();}
    @PutMapping("/members/{id}") public Map<String,Object> updateMember(@PathVariable Long id,@RequestBody Map<String,Object> b){db.update("update member set name=?,phone=?,card_type=?,expire_date=?,status=? where id=?",b.get("name"),b.get("phone"),b.get("card"),b.get("date"),b.get("status"),id);return ok();}
    @DeleteMapping("/members/{id}") public Map<String,Object> deleteMember(@PathVariable Long id){db.update("delete from member where id=?",id);return ok();}
    @GetMapping("/classes") public List<Map<String,Object>> classes(){return db.queryForList("select id,name,coach,class_time time,capacity,current_count current from gym_class order by id");}
    @GetMapping("/checkins") public List<Map<String,Object>> checkins(){return db.queryForList("select c.id,m.name,date_format(c.checkin_time,'%H:%i') time from checkin c join member m on m.id=c.member_id order by c.id desc");}
    @PostMapping("/checkins") public Map<String,Object> checkin(@RequestBody Map<String,Object> b){db.update("insert into checkin(member_id) values(?)",b.get("memberId"));return ok();}
    @GetMapping("/dashboard") public Map<String,Object> dashboard(){Map<String,Object> r=new HashMap<>();r.put("memberCount",db.queryForObject("select count(*) from member",Integer.class));r.put("todayCheckins",db.queryForObject("select count(*) from checkin where date(checkin_time)=curdate()",Integer.class));r.put("classCount",db.queryForObject("select count(*) from gym_class",Integer.class));r.put("monthIncome",68420);return r;}
    private Map<String,Object> ok(){Map<String,Object> r=new HashMap<>();r.put("success",true);return r;}
}
