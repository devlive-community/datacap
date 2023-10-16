const {defineConfig} = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  productionSourceMap: false,
  configureWebpack: {
    plugins: []
  },
  chainWebpack: (config) => {
    if (process.env.NODE_ENV === 'production') {
      config.plugin('webpack-report')
        .use(require('webpack-bundle-analyzer')
          .BundleAnalyzerPlugin, [{analyzerMode: 'static', openAnalyzer: false}]);
      config.plugins.delete('prefetch')
    }
  }
})
