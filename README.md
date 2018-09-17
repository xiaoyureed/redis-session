# redis-session
在分布式环境下, servlet 默认的会话管理就不合适了, 比如 user 登陆到一台机器上, 
如果后续的访问被分配到其他机器, 在这些机器上他会被视作未登录状态. 如果将会话(session)单独存储比如存储到 redis 这种内存数据库中
则可以解决这个问题.

## quick start

```shell
mvn clean compile
mvn jetty:run
```
