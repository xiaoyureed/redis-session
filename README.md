# redis-session
在分布式环境下, servlet 默认的会话管理就不合适了, 比如 user 登陆到一台机器上, 
如果后续的访问被分配到其他机器, 在这些机器上他会被视作未登录状态. 如果将会话(session)单独存储比如存储到 redis 这种内存数据库中
则可以解决这个问题.

主要思路是: 

- 拓展 HttpServletRequest, 实现新的 getSession(xxx) 方法, 原来的是从 cookie 中拿到 jsessionid, 获取 session.
新的方法是从 redis 中拿到 session.

- 通过一个 filter, 拦截特定请求, 替换原来的 request 为 自定义 的新的 request.

- 整个 filter 完成后返回时, 存储 session 到 redis. 

## quick start

```shell
./start.sh
```
