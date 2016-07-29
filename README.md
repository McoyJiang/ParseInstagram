# ParseInstagram
================
<br>
基于Parse云服务端搭建服务器，实现后端服务器，实现简易Instagram项目
---------
<br>
<br>
*效果图*
## 1 点击注册用户时，跳转到用户注册界面。 需要用户输入手机号码，并点击获取验证码，输入手机收到的验证码以及密码之后点击Sign up按钮，向Parse服务端发送网络请求注册用户
![image](https://github.com/McoyJiang/ParseInstagram/raw/master/ParseStarterProject/register2.gif)
<br>
当服务端注册成功之后，可以在Parse服务器的数据库中查看是否已经添加了此用户信息，如下图所示：
![image](https://github.com/McoyJiang/ParseInstagram/raw/master/IMAGES/parse_database.png)
<br>
<br>
<br>
## 2 注册用户成功之后，在主界面，拉出左侧滑菜单，可以看到用户的头像和账号信息。点击头像可以重新设置头像
![image](https://github.com/McoyJiang/ParseInstagram/raw/master/IMAGES/uploadllogo.gif)
<br>
<br>
<br>
## 3 点击上传图片时，会打开Gallery界面显示设备SD卡中的图片，选中需要上传的图片后点击完成
![image](https://github.com/McoyJiang/ParseInstagram/raw/master/IMAGES/uploadmultiple.gif)
<br>
同样，上传成功之后再打开Parse云端数据库，可以发现如下格式的Json字符串<br>
        [
            {
                "__type":"File",
                "name":"9df0646a1e948f98c514e5fefb50d780_Image0.jpg",
                "url":"http://instagram-0720.herokuapp.com/parse/files/jianginstagramUlPFLNRGJfoe/9df0646a1e948f98c514e5fefb50d780_Image0.jpg"
            },
            {
                "__type":"File",
                "name":"c35051e41abf21ce5042a2f6ec04d954_Image1.jpg",
                "url":"http://instagram-0720.herokuapp.com/parse/files/jianginstagramUlPFLNRGJfoe/c35051e41abf21ce5042a2f6ec04d954_Image1.jpg"
            },
            {
                "__type":"File",
                "name":"d4686ccfc65e59b7454037e389a01b49_Image2.jpg",
                "url":"http://instagram-0720.herokuapp.com/parse/files/jianginstagramUlPFLNRGJfoe/d4686ccfc65e59b7454037e389a01b49_Image2.jpg"
            },
            {
                "__type":"File",
                "name":"420247ee2b9d548addfb2992dfba1688_Image3.jpg",
                "url":"http://instagram-0720.herokuapp.com/parse/files/jianginstagramUlPFLNRGJfoe/420247ee2b9d548addfb2992dfba1688_Image3.jpg"
            }
        ]
<br>
<br>
<br>
未完待续。。。
