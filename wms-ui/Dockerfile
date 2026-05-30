# ============================================
# 前端 Dockerfile — Vite Preview Server
# ============================================
FROM node:20-alpine
WORKDIR /app

# 复制已构建好的 dist + 运行时依赖
COPY dist ./dist
COPY package.json ./
COPY vite.config.js ./

# 安装依赖（没有 lock 文件用 npm install）
RUN npm install --ignore-scripts

EXPOSE 4173

CMD ["npx", "vite", "preview", "--host", "0.0.0.0"]
