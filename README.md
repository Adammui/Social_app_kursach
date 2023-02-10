# Course project from 3rd year
java mobile client for blog app; Server: ruby_4_pinb.
### Main functionality:
- Registration and authorization;
- Ability to authorize with Google account;
- User profile editing and Admin capabilities;
- Online chats with other online users;
- Creating posts and comments with pictures, all the posts will be visible on Web version and vice versa;
- Mobile app can store some important posts in local DB, user have to set them as starred in context menu to post.
- Local storage has sync solution in case posts got edited while user was offline. All the editions got time mark and every time user launches app it compares stored copies and updates them or remote ones;
### Used:
- Android Java;
- IDE Android Studio;
- Local Database SQLite;
- Connection to Rails server via http requests (Retrofit library);
## App setup (not going to work as it is, Heroku hosting is now paid and mine is turned off)
Find build apk and install on emulator or with android studio.
> Social_app_kursach/app/release/app-release.apk
## Screenshots
### Log in
![image](https://user-images.githubusercontent.com/53793144/188568730-27c17ed5-250e-4191-82b9-3478f040b888.png)
### chat
![image](https://user-images.githubusercontent.com/53793144/188569312-40385e58-27c5-4207-813a-6ec4a7ce07e7.png)
![image](https://user-images.githubusercontent.com/53793144/188569416-3859965b-5529-48e8-8e7f-28f9d05b921d.png)
### Profile
![image](https://user-images.githubusercontent.com/53793144/188568941-bbdbcdce-dad3-4731-9107-82a484d163f3.png)
### Feed
![image](https://user-images.githubusercontent.com/53793144/188569561-20a1cf9c-9d79-4c6a-8782-f157279835ac.png)
# ru
Андроид клиент для сервера https://github.com/Adammui/ruby_4_pinb
### Основные задачи:
- Регистрация и авторизация пользователей.
- Возможность авторизации через сторонние сервисы (с почтой гугл).
- Обмен сообщениями и медиа с другими авторизированными пользователями в реальном времени.
- В мобильном приложении хранение закладок и важных сообщений в локальной БД, с возможным доступом к ним без подключения к сети.
- Оформление личного профиля пользователя.
### При разработке приложения были использованы:
- Android Java
- IDE Android Studio
- Local Database SQLite
- Connection to Rails server via http requests (Retrofit library)

