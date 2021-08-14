在请求网络数据过程中会需要编写大量的数据模型对象, 映射后端返回的数据创建数据对象. 因为我们业务逻辑中一般是直接数据对象更为方便

创建数据模型我不推荐手写, 错误率高且不方便. 我推荐使用`JSON To Kotlin Class` 插件自动生成

## 安装插件

在Plugins里面搜索kotlin关键词下载安装
<img src="https://i.loli.net/2021/08/14/YLcNQ1vIJPlg82X.png"/>


## 使用插件

选中分包后, 使用快捷键或者鼠标右键打开填写Json界面

<img src="https://i.loli.net/2021/08/14/QZlIHj5WvDz7p2F.png" width="650"/>

添加Json然后点击`Generate`生成

<img src="https://i.loli.net/2021/08/14/sSynJc3paq6CvPw.png" width="750"/>

然后就会在你选中的分包下生成一个数据模型类了

## 高级设置

点击`Advanced`打开设置界面

### 可空选项

截图即为我的推荐配置

<img src="https://i.loli.net/2021/08/14/OSl8jNmGIPngkcJ.png" width="450"/>

1. Keyword 创建的数据模型的字段是 Val还是Var
2. Type 字段是否是可空类型, 最后选项表示根据Json的值判断是否可空
3. 默认值的策略, 无法选择不介绍

### 使用的框架

生成数据模型时会兼容你使用的框架, 例如Moshi和Ks可能需要注解, 然后会自动生成SerialName这些名称注解
<img src="https://i.loli.net/2021/08/14/MrxTilmOX7k6uvt.png" width="450"/>


### 其他

截图即为我的推荐配置

<img src="https://i.loli.net/2021/08/14/gW1a2F5Ilh9emwY.png" width="450"/>

1. 是否使用注释, 既会将Json字符串作为注释保留在数据模型类中
2. 根据字母排序数据模型的字段
3. 使用内部类. 例如Json中的Json对象会作为内部类被创建在数据模型类中 (推荐开启, 保持高内聚低耦合)
4. 如果Json对象的字段都是原始类型则使用Map来表示
5. 只在需要时创建注解
6. 自动验证Json正确性(在填写时)
7. Json格式化时使用的空格数量
8. 指定一个类作为父类模板(既创建的数据模型都会继承该类)

### 扩展

截图即为我的推荐配置

<img src="https://i.loli.net/2021/08/14/MrxTilmOX7k6uvt.png" width="450"/>

1. 添加@Keep注解, 为防止被代码混淆
2. 注解和字段处于同一行, 便于美观
3. 使用Parcelable序列化
4. 为字段添加前缀/后缀
5. 为数据模型类添加前缀/后缀