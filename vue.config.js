const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    // 允许所有主机访问
    allowedHosts: "all",
    // 修复 WebSocket 混合内容警告
    client: {
      webSocketURL: "auto://0.0.0.0:0/ws",
    },

    // 配置代理，解决跨域和 localhost 问题
    proxy: {
      "/recruit": {
        target: "http://localhost:8084", // 你的后端接口地址
        changeOrigin: true,
        ws: true,
      },
    },
  },
});
