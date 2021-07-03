

Android facebbook 投放密钥打印：
（如需要，务必在对接的时候在onCreate（）方法中添加如下代码 ）

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Add code to print out the key hash
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                getPackageName(),
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    ...
```
