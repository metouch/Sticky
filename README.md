# Sticky
一个用于 快速实现吸顶效果，数据按照首字母排序，以及添加侧边索引栏的库
### 效果图预

![1.gif](https://github.com/metouch/Sticky/blob/master/screenshot/1.gif)
### 结构解析

如下图所示

![示意图.png](https://github.com/metouch/Sticky/blob/master/screenshot/%E7%A4%BA%E6%84%8F%E5%9B%BE.png)


将Recyclervie分为四个区域，第一个是未加入索引区的头部，对应的API为

`HeadAdapter.public void addHeaderView(View view, String word, String index);`

word 与 index值均为null

第二个是加入索引区的头部，对应的API为:

`HeadAdapter.public void addHeaderView(View view, String word, String index);`

word 与 index值均不为null

第三个是具有与body部分的item相同item，单索引是特殊字符的部分，对应的API为DataContainer.class

`/**`

` *插入一个middle数据`

` *@param entity               待插入middle数据`

` *@param decorationFirstSpell 分类行显示的文字`

` *param index                导航栏显示文字`

` *param showDecoration 是否显示分隔栏`

` */`

` public void addMiddleData(@NonNull T entity, @NonNull String decorationFirstSpell，@NonNull String index, boolean showDecoration)`

第四部分是主干区，对应的API为DataContainer.class

`//添加body部分数据`

 `public void addData(List<T> entities)`
                                    
### 使用方式

参考sample

数据类需要实现IWor2Spell接口，Adapter类需要继自HeadAdapter
