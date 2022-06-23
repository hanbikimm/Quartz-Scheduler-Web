const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
    devServer: {
    port: 8080,
    proxy: { 
      '^/api': {
        target: 'http://localhost:8000/', 
        changeOrigin: true,
        logLevel: 'debug', // 터미널에 proxy 로그가 찍힌다. 
      },
    },
  },
})
