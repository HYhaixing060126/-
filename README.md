# 健身房管理系统（Java 简单版）

这是一个适合软件专业大专毕设展示的健身房管理系统示例。前端保留了简单的 HTML/CSS/JavaScript，新增的 `backend` 目录使用 Spring Boot 2.7 + MySQL，包含登录、会员、课程、签到和首页统计接口。项目没有 DeepSeek 聊天功能。

## 已完成的功能

- 首页数据看板：会员数、今日签到、月收入、课程数量
- 会员管理：搜索、添加会员、编辑状态、删除会员
- 课程管理：课程列表、教练、上课时间和剩余名额
- 签到管理：会员签到、今日签到记录
- 收支概览：简单的收入统计卡片
- 响应式页面：电脑和手机都可以查看
- Java 后端接口：会员、签到、课程和首页统计
- MySQL 数据库：会员、课程、签到和管理员表
- 登录页：演示账号 `admin`，密码 `123456`

## 运行方式

## Spring Boot + MySQL 后端运行

需要 JDK 8+、Maven 和 MySQL 8。先在 MySQL Workbench 或命令行执行：

```sql
CREATE DATABASE gym_system DEFAULT CHARACTER SET utf8mb4;
```

如果你的 MySQL 账号不是 `root/123456`，修改 `backend/src/main/resources/application.yml` 中的用户名和密码。然后在项目根目录运行：

```bash
cd backend
mvn spring-boot:run
```

后端地址是 `http://localhost:8081`。首次启动会自动创建数据表并写入演示管理员、会员和课程。

再启动原来的前端服务（另开一个终端）：

```bash
java GymManagementServer.java
```

浏览器先打开 `http://localhost:8080/login.html`，使用 `admin / 123456` 登录。

## 只有前端演示时

如果暂时没有 MySQL 或 Maven，仍可在项目根目录执行：

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

Spring Boot 后端接口示例：

```text
POST http://localhost:8081/api/auth/login
GET  http://localhost:8081/api/members
POST http://localhost:8081/api/members
GET  http://localhost:8081/api/checkins
POST http://localhost:8081/api/checkins
GET  http://localhost:8081/api/classes
GET  http://localhost:8081/api/dashboard
```

没有 MySQL 时，浏览器直接打开 `index.html` 仍可使用本地演示数据；登录和 MySQL 数据库功能需要按上面的 Spring Boot 方式启动。
