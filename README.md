## NFormView
A Form View for Android, usage like listView or RecyclerView, different is use AbsFormCell instead of View

[![Jcenter](https://api.bintray.com/packages/nebulae-pan/maven/NFormView/images/download.svg)](https://bintray.com/nebulae-pan/maven/NFormView/_latestVersion)

### Usage
1. add dependencies
```
compile 'nebulae.library.wheel:formview:x.x.x'//version can get from above badges
```

2. extends `BaseAdapter` and implement 

```
int getRowCount();

int getColumnCount();

T createCell(int rowNumber, int colNumber);

void bindCell(T cell, int rowNumber, int colNumber);
```

3. set Adapter to FormView

```
mAdapter = new MyAdapter(/**/);
mFormView.setAdapter(mAdapter);
```

### Function
support NestedScroll(already implement `NestedScrollChild`)

### Todo
add `TitleCell`