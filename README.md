# WMS 仓库管理系统（前后端）

本仓库包含一个完整的仓库管理系统（WMS）：后端为 Spring Boot API，前端为 Vue 3 管理台。

## 1. 项目结构

```
.
├─ src/                         # 后端源码（Spring Boot）
├─ pom.xml                      # 后端 Maven 配置
├─ mvnw / mvnw.cmd              # Maven Wrapper
├─ wms-ui/                      # 前端源码（Vue 3 + Vite）
├─ wms-test.sql                 # 示例数据库结构与数据
└─ openapi.json                 # 导出的 OpenAPI 文档（静态文件）
```

## 2. 技术栈

### 2.1 后端

- Java 17
- Spring Boot 3.4.x
- MyBatis-Plus（Spring Boot 3 Starter）
- Apache Shiro（鉴权/会话）
- MySQL 8.x（运行时）
- EasyExcel（导入/导出）
- springdoc-openapi（接口文档）

### 2.2 前端

- Vue 3 + Vite
- Element Plus
- Vue Router
- Pinia
- Axios

## 3. 环境准备

- JDK 17
- Node.js（建议 20.19+ 或 22.12+，Vite 7 要求）
- MySQL 8.x

## 4. 数据库初始化

1. 创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS `wms` DEFAULT CHARACTER SET utf8mb4;
```

2. 导入示例结构与数据：

```bash
mysql -u <用户名> -p wms < wms-test.sql
```

3. 修改后端数据库配置（可选）：

后端配置文件在 `src/main/resources/application.properties`，默认配置如下，如需修改请调整数据库名、用户名、密码：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wms?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456
```

## 5. 启动后端（API 服务）

后端默认端口为 `8080`，并且所有 `@RestController` 接口都会自动加上 `/api` 前缀。

在项目根目录执行：

```bash
./mvnw spring-boot:run
```

Windows 可用：

```bash
./mvnw.cmd spring-boot:run
```

启动成功后：

- 业务接口示例：`http://localhost:8080/api/dashboard/data`
- OpenAPI JSON：`http://localhost:8080/api/v3/api-docs`
- Swagger UI：`http://localhost:8080/swagger-ui/index.html`

上传/静态资源：

- 运行目录下会使用 `uploads/` 作为上传根目录，并以 `/uploads/**` 对外提供静态访问。

## 6. 启动前端（管理台）

前端位于 `wms-ui/`，Vite 默认端口通常为 `5173`。已配置代理：`/api -> http://localhost:8080`。

```bash
cd wms-ui
npm install
npm run dev
```

启动后访问（以终端输出为准）：

- `http://localhost:5173/`

## 7. 常用构建命令

### 7.1 后端打包

```bash
./mvnw -DskipTests package
```

产物在 `target/WMS-0.0.1-SNAPSHOT.jar`，可直接运行：

```bash
java -jar target/WMS-0.0.1-SNAPSHOT.jar
```

### 7.2 前端构建与预览

```bash
cd wms-ui
npm run build
```

构建产物在 `wms-ui/dist/` 目录。

预览打包后的前端（自动代理 API 到后端）：

```bash
cd wms-ui
npm run preview
```

启动后访问：`http://localhost:4173/`

### 7.3 后端测试

```bash
./mvnw test
```

## 8. 示例账号（来自 wms-test.sql）

示例数据内置了部分用户（登录接口支持用“昵称 / 邮箱 / 手机号”作为账号）：

- 管理员：账号 `admin`，密码 `123456`
- 收货员：账号 `receiver`（或 `receiver@wms.com` / `07210721`），密码 `123456`
- 拣货员：账号 `picker`（或 `picker@wms.com` / `11223344`），密码 `123456`
- 产品管理员：账号 `productM`（或 `PM@wms.com` / `114514`），密码 `123456`
- 仓库管理员：账号 `assetM`（或 `AM@wms.com` / `1026`），密码 `123456`



