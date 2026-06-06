# ============================================
# 前端 Dockerfile — Vite Preview Server
# ============================================
FROM node:20-alpine
WORKDIR /app

# 复制已构建好的 dist + 运行时依赖
COPY dist ./dist
COPY package.json ./
COPY vite.config.js ./

# 安装依赖 — 使用 --mount=type=cache 持久化 npm 缓存
RUN --mount=type=cache,target=/root/.npm,sharing=locked \
    npm install --ignore-scripts

EXPOSE 4173

CMD ["npx", "vite", "preview", "--host", "0.0.0.0"]
