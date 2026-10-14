# 健身房管理系统（Java 简单版）

这是一个适合软件专业大专毕设展示的健身房管理系统示例。项目使用 JDK 自带的 `HttpServer`，没有使用 Spring Boot、数据库或 DeepSeek 聊天功能，便于理解和答辩。后端数据保存在项目下的 `data` 文件夹中。

## 已完成的功能

- 首页数据看板：会员数、今日签到、月收入、课程数量
- 会员管理：搜索、添加会员、编辑状态、删除会员
- 课程管理：课程列表、教练、上课时间和剩余名额
- 签到管理：会员签到、今日签到记录
- 收支概览：简单的收入统计卡片
- 响应式页面：电脑和手机都可以查看
- Java 后端接口：会员、签到、课程和首页统计

## 运行方式

推荐 Java 11 或更高版本。在项目根目录直接执行（Java 11+ 支持单文件运行）：

```bash
java GymManagementServer.java
```

如果电脑安装了完整 JDK，也可以使用传统方式：`javac GymManagementServer.java && java GymManagementServer`。

如果电脑是 Java 8，并且安装了较新的 `javac`，请使用下面的兼容编译命令：

```bash
javac -encoding UTF-8 --release 8 GymManagementServer.java
java GymManagementServer
```

然后打开 <http://localhost:8080>。按 `Ctrl+C` 停止服务。

首次启动会自动创建 `data/members.txt` 和 `data/checkins.txt`。会员接口示例：

```text
GET  http://localhost:8080/api/members
POST http://localhost:8080/api/members
GET  http://localhost:8080/api/checkins
POST http://localhost:8080/api/checkins
GET  http://localhost:8080/api/classes
GET  http://localhost:8080/api/dashboard
```

浏览器直接打开 `index.html` 时仍会使用本地演示数据；通过 `java GymManagementServer.java` 打开页面时，会优先读取 Java 后端的会员数据。
