// see https://webpack.js.org/guides/production/

var path = require('path')
var webpack = require('webpack')
var merge = require('webpack-merge')
var common = require('./webpack.common.js')
var BrowserSyncPlugin = require('browser-sync-webpack-plugin')

var config = {
  // https://webpack.js.org/configuration/plugins/#plugins
  plugins: [
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: '"dev"'
      }
    }),
    // https://www.npmjs.com/package/browser-sync-webpack-plugin#basic
    new BrowserSyncPlugin({
      host: 'localhost',
      port: 3000,
      // Stop the browser from automatically opening
      open: false,
      server: { baseDir: ['public'] }
    })
  ]

};

module.exports = merge(common, config)
