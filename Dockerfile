# ============================================
# 后端 Dockerfile — Spring Boot 3.4 + Java 17
# ============================================

# --- 构建阶段 ---
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 第一步：仅复制 pom.xml，缓存依赖层（pom.xml 不变则此层复用）
COPY pom.xml ./

# 预下载依赖（超时重试，避免网络波动导致卡死）
RUN mvn dependency:resolve -B -q \
    -Dmaven.wagon.http.retryHandler.count=3 \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=25 \
    -Dmaven.wagon.http.retryHandler.requestSentEnabled=true \
    || mvn dependency:resolve -B -q \
    -Dmaven.wagon.http.retryHandler.count=3

# 第二步：复制源码并打包
COPY src ./src
RUN mvn package -DskipTests -B -q

# --- 运行阶段 ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 创建上传目录
RUN mkdir -p /app/uploads /app/data

# 从构建阶段复制 jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
