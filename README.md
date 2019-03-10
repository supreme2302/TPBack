[![Build Status](https://travis-ci.org/supreme2302/TPBack.svg?branch=master)](https://travis-ci.org/supreme2302/TPBack)

<h3>Бэкенд сервера для ТП</h3>

Описание апишки:
<ul>
    <li> Admins - для работы с пользователями на Web (создателями приложения). ("/users")
    <ul>
        <li>GET /admin/info - получение ирнформации о авторизованном пользователе(Эмейл)
        <li>POST /admin/register - регистрация пользователя json : {email:"", pasword:""}
        <li>POST /admin/auth - авторизация пользователя, json тот же что и в регистрации
        <li>POST /admin/change -  изменение пароля пользователя , передаете строчку с новым паролем
    </ul>
    <li> Schools - для создания и администрирования приложений школ, каждой школе соответствует API.("/school")
    <ul>
        <li>POST /school/create - создание школы json: {name:""}
        <li>GET /school/get - получение школы у авторизованного пользователя(ниче не надо, мы по его сессии узнаем) 
    </ul>
    P.S. Надо бы чтобы у школы могло бы быть несколько админов( при помощи промежуточной таблички, или сделав поле school_id и users не уникальной)
    <li> Courses - для просмотра и редактирования созданых курсов.("course")
    <ul>
        <li>POST /course/create - создание курса json: {name: ""}(нам нужно только имя)
        <li>GET /course/ - вытащить названия и id всех курсов данной школы.
        <li>POST /course/delete - удаление курса json: {name: ""}
    </ul>   
    <li> Groups - для просмотра и редактирования групп ("/group/{school_id}/{course_id}")  
    <ul>
        <li>POST /group/newgroup - создание группы у данного курса json: {name: "", courseid: "", start_date: "", curr_unit:" " - необязательно)} 
        <li>GET /group/{course_id} - вывести все группы для данного курса
        <li>POST /group/delete - удаление группы у курса
        <li>POST /group/change - изменение информации о группе json: {name: "",courseid: "", start_date: "", curr_unit:" " - необязательно)}
    </ul>
    <li> Unit - для создания unitов ("/unit/{course_id}"}
    <ul>
        <li>POST /unit/create - создание unita у данного курса json:{name:"", course_id:"", start_date:"", position:""}
        <li>GET /unit/{course_id} - вывод всех unitов данного курса                      
        <li>POST /unit/change - изменение данных в юните
        <li>POST /unit/delete - удаление unita у курса json как и у create
    </ul>
    <li> Students - для создания/изменения студентов ("/student/
    <ul>
        <li>GET /student - вывод всех студентов в школе
        <li>POST /student/create - создание студента json:{ email:"", name:"", surname: "", password: "", group_id: "", school_id: "", phone: ""}
        <li>GET /student/course/{course_id} - вывод студентов в курсе
        <li>GET /student/group/{group_id} - вывод студентов в группе
        <li>POST /student/change - изменение данных студента json как и у create
        <li>POST /student/delete - удаление студента из школы  json{email:} 
        <li>POST /student/login - авторизация студента json: {email:"", password:""}
    </ul>
    P.S. Поиск по фамилии студента надо бы сделать
    <li> Tasks - для создания/изменения заданий ("/task/")
    <ul>
        <li>GET /task/{unit_id} - вывод всех тасков в юните
        <li>POST task/create/ - создание нового таска json:{name:"", descripson:"",type:"",password:"", unit_id:"",task_data:""(это json внутри jsonа для создания таска, в формочке делать будем)}
        <li>GET /task/ - выводит все таски в школе
        <li>POST /task/change - изменение данных tasks json как и у create
        <li>POST /task/delete - удаление tasks из школы  json{id:""} 
    </ul>
</ul>
