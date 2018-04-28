# session-controller

本程序实现了client端向server端发送session的start/stop信息来控制session生命周期的功能。

session在程序中以java bean DeliverySessionCreationType的形式存储，借用第三方库JAXB构建将bean转换为xml字符串的工具类BeanToXml，
并通过其convertToXml方法将session转换为加入http body中的String数据，然后利用第三方库httpClient执行post请求，将数据发送至server端。

其中，为了充分利用计算机性能，利用线程池工具类SessionThreadPool创建ThreadPoolExecutor线程池并利用线程异步并发执行session start数据的发送，
而创建可执行定时任务的ScheduledExecutorService并利用线程异步并发执行根据session中startTime与stopTime的时间间隔延迟发送session stop数据的任务。

且设置连接池将httpClient连接缓存在池中以减少创建连接所需的系统开销，每次都是从连接池中获取有效连接。

整个项目以maven进行管理，session为父模块，整合整个项目，sessionController为client端，server为服务器端。server端使用springboot快速构建简易的服务。

UT方面，只覆盖了client的两个工具类BeanToXml与SendSessionUtil的可调用方法，详见sessionController下的test包
