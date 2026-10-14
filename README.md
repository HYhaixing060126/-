# 健身房管理系统（Java 简单版）

这是一个适合软件专业大专毕设展示的健身房管理系统示例。项目使用 JDK 自带的 `HttpServer`，没有使用 Spring Boot、数据库或 DeepSeek 聊天功能，便于理解和答辩。

## 已完成的功能

- 首页数据看板：会员数、今日签到、月收入、课程数量
- 会员管理：搜索、添加会员、编辑状态、删除会员
- 课程管理：课程列表、教练、上课时间和剩余名额
- 签到管理：会员签到、今日签到记录
- 收支概览：简单的收入统计卡片
- 响应式页面：电脑和手机都可以查看

## 运行方式

需要 Java 11 或更高版本。在项目根目录直接执行（Java 11+ 支持单文件运行）：

```bash
java GymManagementServer.java
```

如果电脑安装了完整 JDK，也可以使用传统方式：`javac GymManagementServer.java && java GymManagementServer`。

然后打开 <http://localhost:8080>。按 `Ctrl+C` 停止服务。

数据保存在浏览器的 `localStorage` 中，适合课堂演示；后续可以把 `GymManagementServer.java` 中的数据替换为 MySQL 读写。
