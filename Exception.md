## 1 RedisTemplate(String,Object)类型，通过get（）得到的Object类型的数据。

`String value = (String)redisTemplate.opsForValue().get("12");`

这是后台的一条语句， Controller层的一个url接口处理。

向前台返回json， 符合一般的Error规范。

具体的过程为： 报异常---ExceptionHandler捕获 + 处理，不是GlobalExcepetion和BindException，所以返回RespEnum.ERROR.

所以得到了ERROR(500,"error")，考虑是普通的异常。

考虑main方法中的以下结构：

``

```
Object o = new Object(); 
String str = (String)o;
```

这个结构会报错ClassCastException。因为Object instance String为false。

``

```
String s = "abc";
Object o = s;
String str = (String)o;
```

以上，从Redis后台传过来的对象本质（使用instanceof）是Object类型的，强行转换会报错。


ps:后来我发现是因为自己用windows的可视化界面， 直接插入 key1，val1,但是json无法识别这里的val1到底是什么格式的， 如果想要是string格式应该设置为“valu1”.

总是，认识到认识是如何产生的，然后定向去解决非常重要。同时，要对内容的深度有所考虑，生产环境，一个无心之举可能导致整个项目无法运行。



