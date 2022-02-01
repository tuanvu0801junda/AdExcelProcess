## Mini web-system processing excel files, a kind of advertising support.

### Folder *spring*
#### Back End, port: 8080
- Using Java Spring, with maven, JDK 11 & apache poi
- After cloning this repo, simply using IntellIJ 
    - Right click at **pom.xml** --> maven --> Reload Project
    - Run Application with IntellIJ
- How to enable **CORS** (Cross-origin Resource Sharing) 
    - [Vue CORS guide](https://www.stackhawk.com/blog/vue-cors-guide-what-it-is-and-how-to-enable-it/) 
    - [CORS Viblo](https://viblo.asia/p/tim-hieu-ve-cross-origin-resource-sharing-cors-Az45bGWqKxY)
    - [Spring Boot CORS](https://hocspringboot.net/2020/12/25/cors-la-gi/)
    - [Sample code Spring Boot](https://github.com/spring-guides/gs-rest-service-cors/blob/main/complete/src/main/java/com/example/restservicecors/RestServiceCorsApplication.java)

### Folder *vuejs*
#### Front End, port: 3333
- Using VueJS Framework
- Set up instruction as below
```
~ Download $ cd vuejs
~ Download/vuejs $ npm i
~ Download/vuejs $ npm run serve
```
- Change default port of VueJs (8080 --> 3333) permanently
    - add devServer: { port: 3333, } in **module.export** at ***vuejs/vue.config.js***
- Set API origin using proxy: 
    - add devServer: { proxy: 'http://127.0.0.1:8080/', } in **module.export** at ***vuejs/vue.config.js***

### Some images about this mini web-system
