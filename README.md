# Sticky

### 效果图预览
![image](https://github.com/metouch/Sticky/raw/master/screenshot/1.gif)
### 设计思路
![image](https://github.com/metouch/Sticky/raw/master/screenshot/%E7%A4%BA%E6%84%8F%E5%9B%BE.png)
如图所示将Recyclervie分为四个区域：
第一个是未加入索引区的头部，对应的API为HeaderAdapter.class下的
```
/**
  *
  * @param view 待添加的头部View
  * @param word decoration的显示字符
  * @param index 索引的显示字符
  * 当word与index为null时，则不添加进索引，作为单独的头部存在
  */
public void addHeaderView(View view, String word, String index)；
```
第二个是加入索引区的头部，对应的API为HeaderAdapter.class下的:
```
/**
  *
  * @param view 待添加的头部View
  * @param word decoration的显示字符
  * @param index 索引的显示字符
  * word与index不为null
  */
public void addHeaderView(View view, String word, String index)；
```
word 与 index值均不为null
第三个区域是具有与body部分的item相同item，单索引是特殊字符的部分，对应的API为DataContainer.class下的

```
/**
  * 插入一组middle数据，
  *
  * @param entities               待插入middle数据
  * @param decorationFirstSpell 分类行显示的文字
  * @param index                导航栏显示文字
  * @param showDecoration 是否显示分隔栏
  */
public void addMiddleData(@NonNull List<T> entities, @NonNull String decorationFirstSpell, @NonNull String index, boolean showDecoration)
```
第四部分是主干区，对应的API为DataContainer.class下的

```
    //添加body部分数据
    public void addData(List<T> entities)
```
### 引入库的方式
在项目工程下的build.gradle文件下添加

```
maven {
    url 'https://dl.bintray.com/metouch/maven'
}
```
添加完后的结构如下图所示

```
allprojects {
    repositories {
        jcenter()
        maven {
            url 'https://dl.bintray.com/metouch/maven'
        }
    }
}
```
在需要引入该库的module下的build.gradle文件中的dependencies下添加

```
compile 'lib.sticky:sticky_lib:1.0.0'
```
### 使用方式
参考sample

数据类需要实现IWor2Spell接口，Adapter类需要继自HeadAdapter
