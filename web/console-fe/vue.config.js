const { defineConfig } = require('@vue/cli-service');
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = defineConfig({
  transpileDependencies: true,
  productionSourceMap: false,
  configureWebpack: {
    plugins: [
      new MonacoWebpackPlugin({
        languages: ['sql'],
      })
    ]
  },
  chainWebpack: (config) => {
    if (process.env.NODE_ENV === 'production') {
      config.plugin('webpack-report')
        .use(require('webpack-bundle-analyzer')
          .BundleAnalyzerPlugin, [{ analyzerMode: 'static', openAnalyzer: false }]);
      config.plugins.delete('prefetch')
    }
  }
})
