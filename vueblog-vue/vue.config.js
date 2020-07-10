module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        'assets': '@/assets',
        'request': '@/request',
        'store': '@/store',
        'router': '@/router',
        'views': '@/views',
        'components': '@/components'
      }
    }
  }
}
