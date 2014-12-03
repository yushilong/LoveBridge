### 代码规范

- 所有Activity请继承于基类YARActivity
- 所有Fragment请继承于基类YARFragment
- 所有实体类请继承于基类YARModel
- 统一用国际化标准引用资源ID
- 全局只存在一个Activity，其他全部为Fragment
- 加边框的ImageView请使用StrokeImageView
- 长按弹出复制框的TextView请使用CopyTextView
- 右边有删除icon的EditText请使用DelIconEditText
- 请求参数工具类YARRequestParamsFactory
- 请求返回结果解析基类YARResponseParser
- HTTP异步请求基类YARRequest 示例：
```java
  JSONObject requestParams = YARRequestParamsFactory.createRequestParams();
  try
  {
      requestParams.put("email", "yuziya@b5m.com");
      requestParams.put("password", "yushilong0921");
      requestParams.put("encrypted", true);
  }
  catch (JSONException e)
  {
      // TODO Auto-generated catch block
      e.printStackTrace();
  }
  new YARRequest("appserver.do?m=login", requestParams, new YARApiListenerImpl()
  {
      @Override
      public void onSuccess(JSONObject response)
      {
          // TODO Auto-generated method stub
          super.onSuccess(response);
          User user = YARResponseParser.parseModel(response, User.class);
      }
  }, mProgressView).start();
```
- HTTP异步分页请求基类YARPageRequest
- 图片异步加载基类YARImageLoader
示例：
```java
YARImageLoader.newInstance().get(iv_photo, "http://b.hiphotos.baidu.com/image/pic/item/eac4b74543a9822606bd2a3a8882b9014a90ebab.jpg");
```
