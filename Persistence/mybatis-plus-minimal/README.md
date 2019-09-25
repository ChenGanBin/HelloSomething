# MyBatis-Plus的最小可用配置

要配置Mybatis-Plus的很简单，特别是使用mybatis-plus-boot-starter的情况下，
只需要实体类及其对应的继承了BaseMapper的接口即可，甚至可以什么都不配置使用默认配置即可，
当然这样并不好，比如说不配置@MapperScan会白白浪费性能增加扫描的时间

测试类中没法使用@Autowired注解，猜测可能为Mapper没有对应的xml文件引起，待验证！
