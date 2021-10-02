# seckill
a simple second kill project, use SpringBoot，Mybatis、Redis、RabbitMQ

## 分布式session的解决
1.当使用分布式的时候， 使用niginx作为网关，进行负载均衡。同一个request可能进到不同的服务器中。如何保证获取session的一致性呢？
这就是分布式session的要解决的问题。

2.常见的解决策略：分为两种，多个tomcat服务器之间同步维护或者创建一个单独地tomcat服务器作为session节点。

**粘性session**：  
apache服务器会把某个用户的请求，交给tomcat集群中的一个节点，以后此节点就负责该保存该用户的session，如果此节点挂掉，那么该用户的session也会消失。    
**非粘性session**：  这种方式下，NGINX通过负载均衡器根据节点的情况，把用户的请求进行分发，复制多份session给多个节点，这样，如果节点中其中一个session挂掉的话，其它的还能继续工作，也就是只要有一个节点没有挂掉，该用户的信息就不会消失。


粘性另解： 这里可以使用redis来进行cookie的存取。

##redis持久化的两种策略

###RDB
快照、两次RDB之间redis服务器发生宕机，会发生数据丢失。RDB的持久化是基于时间的。相比于AOF，可以在宕机之后快速进行数据的恢复。

### AOF
记录操作；

命令--缓冲区--读取到AOF文件，更少可能发生数据丢失现象；

持续性的读取会带来额外的IO压力；文件一般比RDB大，因为同一条数据RDB只有一个记录，而AOF可能有多个操作；


### 持久化策略：RDB + AOF
首先使用RDB，因为当Redis宕机，RDB重启恢复的速度更快。然后使用AOF，把RDB的时间戳之后的操作，写入到Redis数据库之中。

