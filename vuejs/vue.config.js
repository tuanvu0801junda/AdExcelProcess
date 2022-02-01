module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    port: 3333,
    proxy: 'http://127.0.0.1:8080/'
  }
}
